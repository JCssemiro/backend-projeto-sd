package com.juancassemiro.backendprojetosd.Auth.dto;

import jakarta.validation.constraints.NotNull;

public record AuthDto(@NotNull String login,
                      @NotNull String senha) {
}
