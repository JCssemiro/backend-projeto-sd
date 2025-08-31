package com.juancassemiro.backendprojetosd.Disciplina.dto;

import java.util.List;

public record DisciplinaCadastroDto(
        String nome,
        Long professorId,
        List<Long> alunosIds
) {
}
