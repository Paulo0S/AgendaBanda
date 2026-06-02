package br.com.fatec.agendabanda.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LocalRequest(@NotBlank String nome, @NotBlank String endereco, String complemento, @NotNull Integer cidadeId) {
}
