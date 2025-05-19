package br.jus.tjro.gabinete.repository.gab;

import br.jus.tjro.gabinete.model.gab.Aviso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvisoRepository extends JpaRepository<Aviso, Long> {

    @Query("SELECT a from Aviso a where CURRENT_DATE BETWEEN a.dtIniVisibilidade AND a.dtFimVisibilidade AND a.inAtivo = TRUE ")
    List<Aviso> findAtivos();

}
