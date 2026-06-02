package br.com.fatec.agendabanda.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "agenda")
public class Agenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_agenda")
    private Integer codigoAgenda;

    private String titulo;
    private String descricao;

    @Column(name = "dt_hora_in")
    private LocalDateTime dataHoraInicio;

    @Column(name = "dt_hora_fim")
    private LocalDateTime dataHoraFim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_local")
    private LocalEvento local;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_membro_criador")
    private Membro criador;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    public Integer getCodigoAgenda() { return codigoAgenda; }
    public void setCodigoAgenda(Integer codigoAgenda) { this.codigoAgenda = codigoAgenda; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public LocalDateTime getDataHoraInicio() { return dataHoraInicio; }
    public void setDataHoraInicio(LocalDateTime dataHoraInicio) { this.dataHoraInicio = dataHoraInicio; }
    public LocalDateTime getDataHoraFim() { return dataHoraFim; }
    public void setDataHoraFim(LocalDateTime dataHoraFim) { this.dataHoraFim = dataHoraFim; }
    public LocalEvento getLocal() { return local; }
    public void setLocal(LocalEvento local) { this.local = local; }
    public Membro getCriador() { return criador; }
    public void setCriador(Membro criador) { this.criador = criador; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
}
