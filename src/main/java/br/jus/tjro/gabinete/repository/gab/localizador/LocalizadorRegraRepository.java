package br.jus.tjro.gabinete.repository.gab.localizador;

import br.jus.tjro.gabinete.model.gab.localizador.LocalizadorRegra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface LocalizadorRegraRepository extends JpaRepository<LocalizadorRegra, Long> {

    @Query(value = "SELECT LOC_REGRA.* from LOC_REGRA JOIN LOC_CAIXA on LOC_REGRA.ID = LOC_CAIXA.REGRA_ID" +
        " JOIN LOC_CAIXA_LOC_ORGAO_JULGADOR on LOC_CAIXA.ID = LOC_CAIXA_LOC_ORGAO_JULGADOR.LOCALIZADORCAIXA_ID" +
        " JOIN LOC_ORGAO_JULGADOR on LOC_CAIXA_LOC_ORGAO_JULGADOR.ORGAOSJULGADORES_ID_STR = LOC_ORGAO_JULGADOR.ID" +
        " WHERE LOC_ORGAO_JULGADOR.ID = :idOj", nativeQuery = true)
    List<LocalizadorRegra> findByProcesso(@Param("idOj") Long idOj);

    @Query(value = "SELECT LOC_REGRA.* from LOC_REGRA JOIN LOC_CAIXA on LOC_REGRA.ID = LOC_CAIXA.REGRA_ID" +
        " WHERE LOC_CAIXA.ID = :idLocalizador", nativeQuery = true)
    List<LocalizadorRegra> findByLocalizador(@Param("idLocalizador") Long idLocalizador);
}
