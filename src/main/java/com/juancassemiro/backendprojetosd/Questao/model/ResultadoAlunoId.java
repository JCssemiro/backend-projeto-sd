package com.juancassemiro.backendprojetosd.Questao.model;


import com.juancassemiro.backendprojetosd.Usuario.model.Usuario;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class ResultadoAlunoId {

    @ManyToOne
    @JoinColumn(name="id_aluno",referencedColumnName = "id")
    private Usuario aluno;

    @ManyToOne
    @JoinColumn(name="id_questao",referencedColumnName = "id")
    private Questao questao;

}
