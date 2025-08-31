package com.juancassemiro.backendprojetosd.Prova.dto;

import com.juancassemiro.backendprojetosd.Questao.model.ResultadoAluno;
import com.juancassemiro.backendprojetosd.Usuario.dto.UsuarioDadosDto;
import com.juancassemiro.backendprojetosd.Usuario.model.Usuario;

public record ResultadoAlunoDto(Long idAluno, Long idQuestao, String resposta, boolean acertou) {
    public ResultadoAlunoDto(ResultadoAluno resultado) {
        this(
                resultado.getId().getAluno().getId(),
                resultado.getId().getQuestao().getId(),
                resultado.getResposta(),
                resultado.isAcertou()
        );
    }
}