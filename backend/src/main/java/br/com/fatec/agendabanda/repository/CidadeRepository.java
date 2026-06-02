package br.com.fatec.agendabanda.repository;

import br.com.fatec.agendabanda.model.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CidadeRepository extends JpaRepository<Cidade, Integer> {
}
