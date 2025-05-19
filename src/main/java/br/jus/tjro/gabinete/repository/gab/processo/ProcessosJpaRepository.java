package br.jus.tjro.gabinete.repository.gab.processo;

import br.jus.tjro.gabinete.model.gab.processo.Processo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessosJpaRepository extends JpaRepository<Processo, Long> {
}
