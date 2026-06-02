package br.com.fatec.agendabanda.model;

import jakarta.persistence.*;

@Entity
@Table(name = "shows")
public class ShowEvento {
    @Id
    @Column(name = "codigo_agenda")
    private Integer codigoAgenda;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "codigo_agenda")
    private Agenda agenda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_cidade")
    private Cidade cidade;

    public Integer getCodigoAgenda() { return codigoAgenda; }
    public void setCodigoAgenda(Integer codigoAgenda) { this.codigoAgenda = codigoAgenda; }
    public Agenda getAgenda() { return agenda; }
    public void setAgenda(Agenda agenda) { this.agenda = agenda; }
    public Cidade getCidade() { return cidade; }
    public void setCidade(Cidade cidade) { this.cidade = cidade; }
}
