package br.com.fatec.agendabanda.model;

import jakarta.persistence.*;

@Entity
@Table(name = "reunioes")
public class Reuniao {
    @Id
    @Column(name = "codigo_agenda")
    private Integer codigoAgenda;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "codigo_agenda")
    private Agenda agenda;

    private String pauta;

    public Integer getCodigoAgenda() { return codigoAgenda; }
    public void setCodigoAgenda(Integer codigoAgenda) { this.codigoAgenda = codigoAgenda; }
    public Agenda getAgenda() { return agenda; }
    public void setAgenda(Agenda agenda) { this.agenda = agenda; }
    public String getPauta() { return pauta; }
    public void setPauta(String pauta) { this.pauta = pauta; }
}
