package com.juancassemiro.backendprojetosd.infra.security;

import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;

public record TokenDto(@NotNull String token, @NotNull ZonedDateTime dataCriacao, @NotNull ZonedDateTime dataExpiracao){
}
