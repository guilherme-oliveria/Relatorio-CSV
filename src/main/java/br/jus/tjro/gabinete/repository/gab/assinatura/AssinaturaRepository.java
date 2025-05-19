package br.jus.tjro.gabinete.repository.gab.assinatura;

import br.jus.tjro.gabinete.model.gab.minuta.Assinatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssinaturaRepository extends JpaRepository<Assinatura, Long> {
}
