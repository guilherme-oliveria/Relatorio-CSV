package br.jus.tjro.gabinete.repository.gab.minuta;

import br.jus.tjro.gabinete.model.gab.minuta.PublicarProcessoDJE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicarProcessoDJERepository extends JpaRepository<PublicarProcessoDJE, Long> {

}
