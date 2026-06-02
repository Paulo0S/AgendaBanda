package br.com.fatec.agendabanda.repository;

import br.com.fatec.agendabanda.model.Musico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicoRepository extends JpaRepository<Musico, Integer> {
}
