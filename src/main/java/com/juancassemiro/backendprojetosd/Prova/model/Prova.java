package com.juancassemiro.backendprojetosd.Prova.model;


import com.juancassemiro.backendprojetosd.Disciplina.model.Disciplina;
import com.juancassemiro.backendprojetosd.Questao.model.Questao;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Data
@Entity
@Table(name = "prova")
public class Prova {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="id_disciplina",referencedColumnName = "id")
    private Disciplina disciplina;

    private String titulo;

    private String descricao;

    private LocalDate data;

    private int totalQuestoes;

    private Boolean ativa;

    @OneToMany(mappedBy = "prova",cascade = CascadeType.ALL)
    private List<Questao> questoes;

}
