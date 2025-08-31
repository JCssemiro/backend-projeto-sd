package com.juancassemiro.backendprojetosd.Disciplina.dto;

import com.juancassemiro.backendprojetosd.Disciplina.model.Disciplina;
import com.juancassemiro.backendprojetosd.Disciplina.model.Matricula;
import com.juancassemiro.backendprojetosd.Disciplina.model.MatriculaId;
import com.juancassemiro.backendprojetosd.Usuario.dto.UsuarioDadosDto;
import com.juancassemiro.backendprojetosd.Usuario.dto.UsuarioDto;

import java.util.Set;
import java.util.stream.Collectors;

public record DisciplinaDto(
        Long id,
        String nome,
        UsuarioDto professor
) {
    public DisciplinaDto(Disciplina disciplina){
        this(
                disciplina.getId(),
                disciplina.getNome(),
                new UsuarioDto(disciplina.getProfessor())

        );
    }
}
