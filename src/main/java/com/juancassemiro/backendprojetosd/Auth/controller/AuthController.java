package com.juancassemiro.backendprojetosd.Auth.controller;


import com.juancassemiro.backendprojetosd.Auth.dto.AuthDto;
import com.juancassemiro.backendprojetosd.Usuario.model.Usuario;
import com.juancassemiro.backendprojetosd.Usuario.repository.UsuarioRepository;
import com.juancassemiro.backendprojetosd.Usuario.service.UsuarioService;
import com.juancassemiro.backendprojetosd.infra.security.TokenDto;
import com.juancassemiro.backendprojetosd.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    
    private final TokenService tokenService;


    @Autowired
    public AuthController(TokenService tokenService,
                          AuthenticationManager authenticationManager){
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;

    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody @Valid AuthDto data){
        try {
            var UsernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.senha());
            var auth = this.authenticationManager.authenticate(UsernamePassword);

            Usuario usuario = (Usuario) auth.getPrincipal();

            var token = tokenService.generateToken(usuario);
            ZoneId fuso = ZoneId.of("America/Sao_Paulo");
            ZonedDateTime dataCriacao = ZonedDateTime.now(fuso);
            ZonedDateTime dataExpiracao = ZonedDateTime.now(fuso).plusSeconds(60 * 60);
            return ResponseEntity.ok(new TokenDto(token,dataCriacao, dataExpiracao));
        }catch(RuntimeException e){
            throw new LockedException("Login ou Senha Inválidos");
        }
    }


}
