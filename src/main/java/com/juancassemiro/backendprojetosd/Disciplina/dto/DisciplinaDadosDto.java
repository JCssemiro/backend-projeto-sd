package com.juancassemiro.backendprojetosd.Disciplina.dto;

import com.juancassemiro.backendprojetosd.Disciplina.model.Disciplina;
import com.juancassemiro.backendprojetosd.Usuario.dto.UsuarioDto;

public record DisciplinaDadosDto(
        Long id,
        String nome,
        UsuarioDto professor
) {

    public DisciplinaDadosDto(Disciplina disciplina){
        this(
                disciplina.getId(),
                disciplina.getNome(),
                new UsuarioDto(disciplina.getProfessor())
        );
    }
}
