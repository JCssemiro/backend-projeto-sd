package com.juancassemiro.backendprojetosd.Usuario.dto;

import com.juancassemiro.backendprojetosd.Disciplina.dto.DisciplinaDadosDto;
import com.juancassemiro.backendprojetosd.Disciplina.model.Matricula;
import com.juancassemiro.backendprojetosd.Disciplina.model.MatriculaId;
import com.juancassemiro.backendprojetosd.Usuario.model.Usuario;

import java.util.Set;
import java.util.stream.Collectors;

public record UsuarioDadosDto(
        Long id,
        String nome,
        String email,
        String cpf,
        String matriculaGeral
) {

    public UsuarioDadosDto(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getMatricula()
        );
    }
}
