package br.jus.tjro.gabinete.repository.gab.minuta;


import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaAnexo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface MinutasAnexoRepository extends CrudRepository<MinutaAnexo, Long> {

    List<MinutaAnexo> findByMinutaPai(Minuta minuta);

    List<MinutaAnexo> findByMinutaPaiId(Long minutaId);

    @Query("update MinutaAnexo ma set ma.sigilo = :sigilo where ma.id = :id")
    int atualizaSigilo(@Param("sigilo") boolean sigilo, @Param("id") Long id);

    @Query("SELECT count(ma.id) FROM MinutaAnexo ma where ma.minutaPai.id = :minutaId ")
    Integer nextPosition(@Param("minutaId") Long minutaId);

    @Modifying
    @Query("update MinutaAnexo ma set ma.idMinutaSistemaLegado = null where ma.id = :id")
    int removeIdLegado(@Param("id") Long idDocumento);
}
