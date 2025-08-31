package com.juancassemiro.backendprojetosd.Questao.repository;

import com.juancassemiro.backendprojetosd.Questao.model.Questao;
import com.juancassemiro.backendprojetosd.Questao.model.ResultadoAluno;
import com.juancassemiro.backendprojetosd.Questao.model.ResultadoAlunoId;
import com.juancassemiro.backendprojetosd.Usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ResultadoAlunoRepository extends JpaRepository<ResultadoAluno, ResultadoAlunoId> {

    @Query("SELECT r FROM ResultadoAluno r WHERE r.id.aluno =:aluno AND r.id.questao IN :questao")
    List<ResultadoAluno> findByIdAlunoAndInListQuestao(Usuario aluno, List<Questao> questao);
}
