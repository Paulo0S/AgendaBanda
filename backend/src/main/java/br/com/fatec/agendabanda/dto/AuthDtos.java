package br.com.fatec.agendabanda.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthDtos {
    public record LoginRequest(@NotBlank String login, @NotBlank String senha) {}
    public record LoginResponse(String token, MembroDto usuario) {}
}
