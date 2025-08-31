package com.juancassemiro.backendprojetosd.Prova.dto;

import com.juancassemiro.backendprojetosd.Questao.dto.QuestaoCadastroDto;

import java.time.LocalDate;
import java.util.List;

public record ProvaCadastroDto(
        Long disciplinaId,
        String titulo,
        String descricao,
        LocalDate data,
        List<QuestaoCadastroDto> questoes,
        Boolean ativa
) {
}
