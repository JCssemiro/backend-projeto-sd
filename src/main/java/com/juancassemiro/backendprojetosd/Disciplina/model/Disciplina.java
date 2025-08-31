package com.juancassemiro.backendprojetosd.Disciplina.model;


import com.juancassemiro.backendprojetosd.Disciplina.dto.DisciplinaCadastroDto;
import com.juancassemiro.backendprojetosd.Usuario.model.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="disciplina")
public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToOne
    @JoinColumn(name="id_professor",referencedColumnName = "id")
    private Usuario professor;

    @OneToMany(mappedBy = "id.disciplina",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Matricula> matriculas;

    public Disciplina(DisciplinaCadastroDto disciplinaDTO) {
        this.nome = disciplinaDTO.nome();
        this.professor = new Usuario(disciplinaDTO.professorId());
    }

}
