package com.juancassemiro.backendprojetosd.Prova.dto;

import com.juancassemiro.backendprojetosd.Disciplina.dto.DisciplinaDadosDto;
import com.juancassemiro.backendprojetosd.Prova.model.Prova;
import com.juancassemiro.backendprojetosd.Questao.dto.QuestaoDto;

import java.time.LocalDate;
import java.util.List;

public record ProvaAlunoDto(
        Long id,
        DisciplinaDadosDto disciplina,
        String titulo,
        String descricao,
        LocalDate data,
        Integer totalQuestoes,
        Boolean ativa,
        Boolean jaRespondida,
        List<QuestaoDto> questoes
) {

    public ProvaAlunoDto(Prova prova,Boolean jaRespondida){
        this(
          prova.getId(),
          new DisciplinaDadosDto(prova.getDisciplina()),
          prova.getTitulo(),
          prova.getDescricao(),
          prova.getData(),
          prova.getTotalQuestoes(),
          prova.getAtiva(),
          jaRespondida,
          prova.getQuestoes().stream().map(QuestaoDto::new).toList()
        );
    }
}
