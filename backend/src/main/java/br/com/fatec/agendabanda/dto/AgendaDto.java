package br.com.fatec.agendabanda.dto;

import java.time.LocalDateTime;
import java.util.List;

public record AgendaDto(
        Integer codigoAgenda,
        String titulo,
        String descricao,
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        LocalDto local,
        MembroDto criador,
        String tipoEspecifico,
        String repertorio,
        String observacao,
        String pauta,
        CidadeDto cidadeShow,
        List<MembroDto> musicos
) {
}
