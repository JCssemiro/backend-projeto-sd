package com.juancassemiro.backendprojetosd.Usuario.dto;

import jakarta.validation.constraints.NotNull;

public record UsuarioCadastroDto(
        @NotNull String nome,
        @NotNull String email,
        @NotNull String senha,
        @NotNull String cpf,
        @NotNull String matricula,
        @NotNull     Long tipoUsuario
){
}
