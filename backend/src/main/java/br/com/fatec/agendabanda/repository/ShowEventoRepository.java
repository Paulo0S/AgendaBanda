package br.com.fatec.agendabanda.repository;

import br.com.fatec.agendabanda.model.ShowEvento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowEventoRepository extends JpaRepository<ShowEvento, Integer> {
}
