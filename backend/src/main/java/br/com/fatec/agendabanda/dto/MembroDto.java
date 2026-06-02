package br.com.fatec.agendabanda.dto;

public record MembroDto(
        Integer codigo,
        String nome,
        String cpf,
        String email,
        String login,
        Boolean administrador,
        Boolean ativo,
        String perfil,
        String instrumento,
        String biografia,
        String especialidade
) {
}
