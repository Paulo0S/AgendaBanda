package br.com.fatec.agendabanda.repository;

import br.com.fatec.agendabanda.model.Produtor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutorRepository extends JpaRepository<Produtor, Integer> {
}
