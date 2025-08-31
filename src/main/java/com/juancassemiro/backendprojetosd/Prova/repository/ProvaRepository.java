package com.juancassemiro.backendprojetosd.Prova.repository;

import com.juancassemiro.backendprojetosd.Prova.model.Prova;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProvaRepository extends JpaRepository<Prova,Long> {

    @Query("SELECT p FROM Prova p WHERE p.disciplina.id IN :disciplinasIds")
    List<Prova> findByDisciplinaIdIn(List<Long> disciplinasIds);
}
