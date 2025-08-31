package com.juancassemiro.backendprojetosd.Questao.model;


import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="resultadoaluno")
public class ResultadoAluno {

    @EmbeddedId
    private ResultadoAlunoId id;

    @Column(name="resposta_aluno",nullable = false)
    private String resposta;

    private boolean acertou;

}
