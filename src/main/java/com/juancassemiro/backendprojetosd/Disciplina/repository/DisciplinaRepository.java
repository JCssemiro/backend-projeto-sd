package com.juancassemiro.backendprojetosd.Disciplina.repository;

import com.juancassemiro.backendprojetosd.Disciplina.model.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina,Long> {
}
