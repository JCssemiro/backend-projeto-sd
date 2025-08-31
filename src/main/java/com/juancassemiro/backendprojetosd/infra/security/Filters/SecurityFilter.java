package com.juancassemiro.backendprojetosd.infra.security.Filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juancassemiro.backendprojetosd.Usuario.model.Usuario;
import com.juancassemiro.backendprojetosd.Usuario.repository.UsuarioRepository;
import com.juancassemiro.backendprojetosd.infra.security.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        if(token != null){
            var login = tokenService.validateToken(token);
            if(login.isEmpty()){
                    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
                    problemDetail.setTitle("Token Expirado ou Inválido");
                    problemDetail.setDetail("Tente entrar novamente");
                    problemDetail.setInstance(URI.create(request.getRequestURI()));
                    response.setStatus(403);
                    response.setContentType("application/json");
                    writeResponse(response, problemDetail);
                    return;
            }

            Optional<Usuario> usuario = usuarioRepository.findByLogin(login);
            if(usuario.isPresent()){
                    var authentication = new UsernamePasswordAuthenticationToken(usuario.get().getUsername(),null, usuario.get().getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
            }else {
                System.out.println("Usuário ou senha Inválidos!");
                ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
                problemDetail.setTitle("Usuário ou senha inválidos!");
                problemDetail.setDetail("Tente entrar novamente");
                problemDetail.setInstance(URI.create(request.getRequestURI()));
                response.setStatus(403);
                response.setContentType("application/json");
                writeResponse(response, problemDetail);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }

    private void writeResponse(HttpServletResponse response, ProblemDetail problemDetail) throws IOException {
        OutputStream out = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out,problemDetail);
        out.flush();
    }
}