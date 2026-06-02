package br.com.fatec.agendabanda.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ensaios")
public class Ensaio {
    @Id
    @Column(name = "codigo_agenda")
    private Integer codigoAgenda;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "codigo_agenda")
    private Agenda agenda;

    private String repertorio;
    private String observacao;

    public Integer getCodigoAgenda() { return codigoAgenda; }
    public void setCodigoAgenda(Integer codigoAgenda) { this.codigoAgenda = codigoAgenda; }
    public Agenda getAgenda() { return agenda; }
    public void setAgenda(Agenda agenda) { this.agenda = agenda; }
    public String getRepertorio() { return repertorio; }
    public void setRepertorio(String repertorio) { this.repertorio = repertorio; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}
