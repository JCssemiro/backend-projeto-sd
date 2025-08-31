package com.juancassemiro.backendprojetosd.Usuario.model;


import com.juancassemiro.backendprojetosd.Disciplina.model.Disciplina;
import com.juancassemiro.backendprojetosd.Disciplina.model.Matricula;
import com.juancassemiro.backendprojetosd.Questao.model.Questao;
import com.juancassemiro.backendprojetosd.Questao.model.ResultadoAluno;
import com.juancassemiro.backendprojetosd.TipoUsuario.model.TipoUsuario;
import com.juancassemiro.backendprojetosd.Usuario.dto.UsuarioCadastroDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name="usuario")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;
    private String cpf;
    @Column(name="matricula_geral")
    private String matricula;

    @ManyToOne
    @JoinColumn(name="id_tipousuario",referencedColumnName = "id")
    private TipoUsuario tipoUsuario;

    // Relacionamento com ResultadoAluno usando a chave composta
    @OneToMany(mappedBy = "id.aluno", cascade = CascadeType.ALL, orphanRemoval = true) // MappedBy agora aponta para o campo 'aluno' dentro do 'id' (ResultadoAlunoId)
    private List<ResultadoAluno> resultadosAluno;


    @OneToMany(mappedBy = "id.aluno")
    private List<Matricula> matriculas;

    public Usuario(UsuarioCadastroDto dto, String senha) {
        this.nome = dto.nome();
        this.email = dto.email();
        this.senha = senha;
        this.cpf = dto.cpf();
        this.matricula = dto.matricula();
    }

    public Usuario(Long id) {
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(this.tipoUsuario);
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}