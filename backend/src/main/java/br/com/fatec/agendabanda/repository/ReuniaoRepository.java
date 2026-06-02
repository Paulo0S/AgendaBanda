package br.com.fatec.agendabanda.repository;

import br.com.fatec.agendabanda.model.Reuniao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReuniaoRepository extends JpaRepository<Reuniao, Integer> {
}
