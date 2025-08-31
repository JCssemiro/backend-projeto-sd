package com.juancassemiro.backendprojetosd.Prova.dto;

import com.juancassemiro.backendprojetosd.Disciplina.dto.DisciplinaDadosDto;
import com.juancassemiro.backendprojetosd.Prova.model.Prova;
import com.juancassemiro.backendprojetosd.Questao.dto.QuestaoDto;
import com.juancassemiro.backendprojetosd.Questao.model.Questao;
import com.juancassemiro.backendprojetosd.Usuario.dto.UsuarioDadosDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record ProvaDto(
        Long id,
        DisciplinaDadosDto disciplina,
        String titulo,
        String descricao,
        LocalDate data,
        Integer totalQuestoes,
        List<QuestaoDto> questoes,
        List<ResultadoAlunoDto> resultados
) {

    public ProvaDto(Prova prova){
        this(
                prova.getId(),
                new DisciplinaDadosDto(prova.getDisciplina()),
                prova.getTitulo(),
                prova.getDescricao(),
                prova.getData(),
                prova.getTotalQuestoes(),
                prova.getQuestoes().stream().map(QuestaoDto::new).toList(),
                // Mapeia todos os resultados de todas as questões
                prova.getQuestoes().stream()
                        .flatMap(q -> q.getResultadosAluno().stream())
                        .map(ResultadoAlunoDto::new)
                        .toList()
        );
    }

}
