package br.jus.tjro.gabinete.repository.gab.alvara;

import br.jus.tjro.gabinete.model.gab.alvara.Alvara;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlvaraRepository extends JpaRepository<Alvara, Long> {
    Optional<Alvara> findByMinutaId(Long id);
}
