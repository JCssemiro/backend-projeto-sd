package com.juancassemiro.backendprojetosd.Usuario.dto;


import com.juancassemiro.backendprojetosd.Disciplina.dto.DisciplinaDadosDto;
import com.juancassemiro.backendprojetosd.Disciplina.model.Matricula;
import com.juancassemiro.backendprojetosd.Disciplina.model.MatriculaId;
import com.juancassemiro.backendprojetosd.Usuario.model.Usuario;

import java.util.Set;
import java.util.stream.Collectors;

public record UsuarioDto(
        Long id,
        String nome,
        String email,
        String cpf,
        String matriculaGeral,
        Set<DisciplinaDadosDto> disciplinas
) {

    public UsuarioDto(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getMatricula(),
                usuario.getMatriculas() != null ? usuario.getMatriculas().stream().map(Matricula::getId).map(MatriculaId::getDisciplina).map(DisciplinaDadosDto::new).collect(Collectors.toSet()) : null
        );;
    }
}
