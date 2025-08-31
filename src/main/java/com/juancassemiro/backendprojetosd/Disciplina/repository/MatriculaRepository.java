package com.juancassemiro.backendprojetosd.Disciplina.repository;

import com.juancassemiro.backendprojetosd.Disciplina.model.Matricula;
import com.juancassemiro.backendprojetosd.Disciplina.model.MatriculaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, MatriculaId> {

    @Modifying
    @Query("DELETE FROM Matricula m WHERE m.id.disciplina.id=:id")
    void deleteAllByIdDisciplina(Long id);
}
