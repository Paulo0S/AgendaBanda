package br.com.fatec.agendabanda.repository;

import br.com.fatec.agendabanda.model.EnsaioMusico;
import br.com.fatec.agendabanda.model.EnsaioMusicoId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnsaioMusicoRepository extends JpaRepository<EnsaioMusico, EnsaioMusicoId> {
    List<EnsaioMusico> findByEnsaioCodigoAgenda(Integer codigoAgenda);
    void deleteByEnsaioCodigoAgenda(Integer codigoAgenda);
}
