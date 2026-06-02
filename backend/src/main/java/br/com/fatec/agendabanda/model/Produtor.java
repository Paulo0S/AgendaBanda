package br.com.fatec.agendabanda.model;

import jakarta.persistence.*;

@Entity
@Table(name = "produtores")
public class Produtor {
    @Id
    @Column(name = "codigo")
    private Integer codigo;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "codigo")
    private Membro membro;

    private String especialidade;

    public Integer getCodigo() { return codigo; }
    public void setCodigo(Integer codigo) { this.codigo = codigo; }
    public Membro getMembro() { return membro; }
    public void setMembro(Membro membro) { this.membro = membro; }
    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
}
