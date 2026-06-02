package br.com.fatec.agendabanda.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cidades")
public class Cidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_cidade")
    private Integer codigoCidade;

    private String nome;
    private String uf;

    public Integer getCodigoCidade() { return codigoCidade; }
    public void setCodigoCidade(Integer codigoCidade) { this.codigoCidade = codigoCidade; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }
}
