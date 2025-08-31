package com.juancassemiro.backendprojetosd.Disciplina.model;


import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="matricula")
public class Matricula {

    @EmbeddedId
    private MatriculaId id;

    private int status;
}
