package br.jus.tjro.gabinete.repository.gab.processo;

import br.jus.tjro.gabinete.model.gab.processo.PrioridadeProcessual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface PrioridadesProcessuaisRepository extends JpaRepository<PrioridadeProcessual, Integer> {

    Optional<PrioridadeProcessual> findById(Integer prioridade);

}
