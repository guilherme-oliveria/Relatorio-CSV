package br.jus.tjro.gabinete.repository.gab.alvara;

import br.jus.tjro.gabinete.model.gab.alvara.PagamentoAlvara;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoAlvaraRepository extends JpaRepository<PagamentoAlvara, Long> {
}
