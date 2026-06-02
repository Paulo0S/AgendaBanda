package br.com.fatec.agendabanda.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MembroRequest(
        @NotBlank String nome,
        @NotBlank String cpf,
        @Email @NotBlank String email,
        @NotBlank String login,
        String senha,
        Boolean administrador,
        Boolean ativo,
        String perfil,
        String instrumento,
        String biografia,
        String especialidade
) {
}
