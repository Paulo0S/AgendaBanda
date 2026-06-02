package br.com.fatec.agendabanda.repository;

import br.com.fatec.agendabanda.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda, Integer> {
    List<Agenda> findAllByOrderByDataHoraInicioAsc();
}
