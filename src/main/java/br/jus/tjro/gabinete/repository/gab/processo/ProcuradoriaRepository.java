package br.jus.tjro.gabinete.repository.gab.processo;

import br.jus.tjro.gabinete.model.gab.processo.Procuradoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcuradoriaRepository extends JpaRepository<Procuradoria, Long> {

    Optional<Procuradoria> findByIdLegado(Long idLegado);
}
