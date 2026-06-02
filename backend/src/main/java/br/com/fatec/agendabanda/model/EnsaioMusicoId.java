package br.com.fatec.agendabanda.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EnsaioMusicoId implements Serializable {
    @Column(name = "codigo_agenda")
    private Integer codigoAgenda;

    @Column(name = "cod_musico")
    private Integer codMusico;

    public EnsaioMusicoId() {}
    public EnsaioMusicoId(Integer codigoAgenda, Integer codMusico) {
        this.codigoAgenda = codigoAgenda;
        this.codMusico = codMusico;
    }

    public Integer getCodigoAgenda() { return codigoAgenda; }
    public void setCodigoAgenda(Integer codigoAgenda) { this.codigoAgenda = codigoAgenda; }
    public Integer getCodMusico() { return codMusico; }
    public void setCodMusico(Integer codMusico) { this.codMusico = codMusico; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnsaioMusicoId that)) return false;
        return Objects.equals(codigoAgenda, that.codigoAgenda) && Objects.equals(codMusico, that.codMusico);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoAgenda, codMusico);
    }
}
