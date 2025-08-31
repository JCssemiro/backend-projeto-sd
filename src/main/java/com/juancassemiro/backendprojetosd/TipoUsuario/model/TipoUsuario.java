package com.juancassemiro.backendprojetosd.TipoUsuario.model;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
@Entity
@Table(name="tipousuario")
public class TipoUsuario implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    @Override
    public String getAuthority() {
        return this.descricao;
    }
}
