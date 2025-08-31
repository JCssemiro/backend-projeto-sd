package com.juancassemiro.backendprojetosd.Questao.repository;

import com.juancassemiro.backendprojetosd.Questao.model.Questao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestaoRepository extends JpaRepository<Questao,Long> {

    @Modifying
    @Query("DELETE FROM Questao q WHERE q.prova.id =:id")
    void deleteAllByProvaId(Long id);
}
