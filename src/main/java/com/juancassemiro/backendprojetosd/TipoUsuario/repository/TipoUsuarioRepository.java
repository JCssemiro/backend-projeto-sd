package com.juancassemiro.backendprojetosd.TipoUsuario.repository;


import com.juancassemiro.backendprojetosd.TipoUsuario.model.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario,Long> {
}
