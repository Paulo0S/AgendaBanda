package br.com.fatec.agendabanda.repository;

import br.com.fatec.agendabanda.model.Membro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembroRepository extends JpaRepository<Membro, Integer> {
    Optional<Membro> findByLogin(String login);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
}
