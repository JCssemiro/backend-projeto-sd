package com.juancassemiro.backendprojetosd.Usuario.service;


import com.juancassemiro.backendprojetosd.TipoUsuario.model.TipoUsuario;
import com.juancassemiro.backendprojetosd.TipoUsuario.repository.TipoUsuarioRepository;
import com.juancassemiro.backendprojetosd.Usuario.dto.UsuarioCadastroDto;
import com.juancassemiro.backendprojetosd.Usuario.dto.UsuarioDto;
import com.juancassemiro.backendprojetosd.Usuario.model.Usuario;
import com.juancassemiro.backendprojetosd.Usuario.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository,
                          TipoUsuarioRepository tipoUsuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cria um novo usuário.
     * @param usuarioDTO DTO com os dados do usuário a ser criado.
     * @return O DTO do usuário criado.
     */
    @Transactional
    public UsuarioDto criarUsuario(UsuarioCadastroDto usuarioDTO) {
        Usuario usuario = new Usuario(usuarioDTO,passwordEncoder.encode(usuarioDTO.senha()));

        // Lógica para lidar com TipoUsuario, se o DTO tiver apenas o ID
        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(usuarioDTO.tipoUsuario())
                .orElseThrow(() -> new NoSuchElementException("Tipo de Usuário não encontrado")); // Ou uma exceção mais específica
        usuario.setTipoUsuario(tipoUsuario);


        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return new UsuarioDto(usuarioSalvo);
    }


    /**
     * Busca um usuário pelo ID.
     * @param id ID do usuário.
     * @return O DTO do usuário, se encontrado, ou Optional vazio.
     */
    public UsuarioDto buscarUsuarioPorId(Long id) {
        return new UsuarioDto(usuarioRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("Usuário não encontrado")));
    }


    /**
     * Deleta um usuário pelo ID.
     * @param id ID do usuário a ser deletado.
     * @throws RuntimeException Se o usuário não for encontrado.
     */
    @Transactional
    public void deletarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado com ID: " + id); // Ou uma exceção mais específica
        }
        usuarioRepository.deleteById(id);
    }


}
