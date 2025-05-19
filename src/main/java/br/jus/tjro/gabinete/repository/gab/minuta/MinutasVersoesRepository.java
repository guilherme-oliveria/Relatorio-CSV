package br.jus.tjro.gabinete.repository.gab.minuta;

import br.jus.tjro.gabinete.model.gab.minuta.MinutaVersao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import java.util.List;

@Transactional
public interface MinutasVersoesRepository extends JpaRepository<MinutaVersao, Long> {

    @Query("SELECT "
        + "versao "
        + "from MinutaVersao versao "
        + "where versao.minuta.id = :idMinuta "
        + "order by versao.versao desc")
    List<MinutaVersao> findByIdMinuta(@Param("idMinuta") Long idMinuta);

    @Query("SELECT "
        + "versao_compara "
        + "from MinutaVersao versao_compara "
        + "where versao_compara.minuta.id = :idMinuta "
        + "and versao_compara.versao = :versao" )
    MinutaVersao findByIdMinutaVersao(@Param("idMinuta") Long idMinuta, @Param("versao") Integer versao);

}
