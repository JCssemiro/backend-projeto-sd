package com.juancassemiro.backendprojetosd.Questao.dto;

import com.juancassemiro.backendprojetosd.Questao.model.Questao;

public record QuestaoDto(
        Long id,
        String descricao,
        String alternativaCorreta,
        String alternativaA,
        String alternativaB,
        String alternativaC,
        String alternativaD,
        String alternativaE
) {

    public QuestaoDto(Questao questao){
        this(
                questao.getId(),
                questao.getDescricao(),
                questao.getAlternativaCorreta(),
                questao.getAlternativaA(),
                questao.getAlternativaB(),
                questao.getAlternativaC(),
                questao.getAlternativaD(),
                questao.getAlternativaE()
        );
    }
}
