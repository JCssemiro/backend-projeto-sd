package com.juancassemiro.backendprojetosd.Disciplina.model;

import com.juancassemiro.backendprojetosd.Usuario.model.Usuario;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MatriculaId {

    @ManyToOne
    @JoinColumn(name = "id_aluno",referencedColumnName = "id")
    private Usuario aluno;

    @ManyToOne
    @JoinColumn(name="id_disciplina",referencedColumnName = "id")
    private Disciplina disciplina;
}
