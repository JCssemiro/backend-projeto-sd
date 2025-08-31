package com.juancassemiro.backendprojetosd.Usuario.repository;

import com.juancassemiro.backendprojetosd.Usuario.model.Usuario;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    @Query("SELECT u FROM Usuario u WHERE u.email =:login")
    Optional<Usuario> findByLogin(String login);
}
