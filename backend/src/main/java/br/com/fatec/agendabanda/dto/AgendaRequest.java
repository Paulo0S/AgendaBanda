package br.com.fatec.agendabanda.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record AgendaRequest(
        @NotBlank String titulo,
        String descricao,
        @NotNull LocalDateTime dataHoraInicio,
        @NotNull LocalDateTime dataHoraFim,
        @NotNull Integer localId,
        @NotBlank String tipoEspecifico,
        String repertorio,
        String observacao,
        String pauta,
        Integer cidadeId,
        List<Integer> musicosIds
) {
}
