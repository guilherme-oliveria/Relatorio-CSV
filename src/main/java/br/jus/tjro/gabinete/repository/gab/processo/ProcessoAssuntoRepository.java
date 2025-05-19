package br.jus.tjro.gabinete.repository.gab.processo;

import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoAssunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface ProcessoAssuntoRepository extends JpaRepository<ProcessoAssunto, Long> {

    List<ProcessoAssunto> findByProcesso(Processo processo);

    @Transactional(readOnly = true)
    @Query(value = ""
        + "SELECT "
        + "pa.idAssunto "
        + "FROM ProcessoAssunto pa "
        + "WHERE "
        + "pa.processo.id = :idProcesso ")
    List<Long> findIdsAssuntos(@Param("idProcesso") Long idProcesso);

    ProcessoAssunto findByProcessoAndIdAssunto(Processo processo, Long idAssunto);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM ProcessoAssunto pa WHERE pa.processo = :processo AND pa.idAssunto NOT IN (:assuntos) ")
    void deleteAllByProcessoIdAndIdAssuntoNotIn(@Param("processo") Processo processo, @Param("assuntos") List<Long> assuntos);
}
