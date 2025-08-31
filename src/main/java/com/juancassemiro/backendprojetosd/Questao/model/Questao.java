package com.juancassemiro.backendprojetosd.Questao.model;

import com.juancassemiro.backendprojetosd.Prova.model.Prova;
import com.juancassemiro.backendprojetosd.Questao.dto.QuestaoCadastroDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Data
@Entity
@Table(name="questao")
public class Questao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    @ManyToOne
    @JoinColumn(name="id_prova",referencedColumnName = "id")
    private Prova prova;

    private String alternativaCorreta;

    @Column(name="alternativa_a")
    private String alternativaA;

    @Column(name="alternativa_b")
    private String alternativaB;

    @Column(name="alternativa_c")
    private String alternativaC;

    @Column(name="alternativa_d")
    private String alternativaD;

    @Column(name="alternativa_e")
    private String alternativaE;

    // Relacionamento com ResultadoAluno usando a chave composta
    @OneToMany(mappedBy = "id.questao", cascade = CascadeType.ALL, orphanRemoval = true) // MappedBy agora aponta para o campo 'questao' dentro do 'id' (ResultadoAlunoId)
    private List<ResultadoAluno> resultadosAluno;

    public Questao(QuestaoCadastroDto dto) {
        this.descricao = dto.descricao();
        this.alternativaCorreta = dto.alternativaCorreta();
        this.alternativaA = dto.alternativaA();
        this.alternativaB = dto.alternativaB();
        this.alternativaC = dto.alternativaC();
        this.alternativaD = dto.alternativaD();
        this.alternativaE = dto.alternativaE();
    }

}
