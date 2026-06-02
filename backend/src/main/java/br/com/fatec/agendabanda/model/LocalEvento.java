package br.com.fatec.agendabanda.model;

import jakarta.persistence.*;

@Entity
@Table(name = "locais")
public class LocalEvento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_local")
    private Integer codigoLocal;

    private String nome;
    private String endereco;
    private String complemento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_cidade")
    private Cidade cidade;

    public Integer getCodigoLocal() { return codigoLocal; }
    public void setCodigoLocal(Integer codigoLocal) { this.codigoLocal = codigoLocal; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }
    public Cidade getCidade() { return cidade; }
    public void setCidade(Cidade cidade) { this.cidade = cidade; }
}
