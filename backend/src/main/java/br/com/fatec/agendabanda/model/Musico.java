package br.com.fatec.agendabanda.model;

import jakarta.persistence.*;

@Entity
@Table(name = "musicos")
public class Musico {
    @Id
    @Column(name = "codigo")
    private Integer codigo;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "codigo")
    private Membro membro;

    private String instrumento;
    private String biografia;

    public Integer getCodigo() { return codigo; }
    public void setCodigo(Integer codigo) { this.codigo = codigo; }
    public Membro getMembro() { return membro; }
    public void setMembro(Membro membro) { this.membro = membro; }
    public String getInstrumento() { return instrumento; }
    public void setInstrumento(String instrumento) { this.instrumento = instrumento; }
    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }
}
