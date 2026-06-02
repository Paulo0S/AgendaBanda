package br.com.fatec.agendabanda.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ensaio_musicos")
public class EnsaioMusico {
    @EmbeddedId
    private EnsaioMusicoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codigoAgenda")
    @JoinColumn(name = "codigo_agenda")
    private Ensaio ensaio;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codMusico")
    @JoinColumn(name = "cod_musico")
    private Musico musico;

    public EnsaioMusicoId getId() { return id; }
    public void setId(EnsaioMusicoId id) { this.id = id; }
    public Ensaio getEnsaio() { return ensaio; }
    public void setEnsaio(Ensaio ensaio) { this.ensaio = ensaio; }
    public Musico getMusico() { return musico; }
    public void setMusico(Musico musico) { this.musico = musico; }
}
