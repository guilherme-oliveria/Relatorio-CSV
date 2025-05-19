package br.jus.tjro.gabinete.repository.gab.minuta;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaTarefaLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MinutaTarefaLogRepository extends JpaRepository<MinutaTarefaLog, Long> {

    @Query("SELECT   "
        + " minutaTarefaLog " +
        "from MinutaTarefaLog minutaTarefaLog " +
        "where minutaTarefaLog.minuta.id = :idMinuta " +
        "order by minutaTarefaLog.dataLog desc "
        )
    List<MinutaTarefaLog> findUltimoMinutaTarefaLogByIdMinuta(@Param("idMinuta") Long idMinuta, Pageable pageable);

    List<MinutaTarefaLog> findByMinutaIdAndTarefaEnumOrderByDataLogDesc(Long id, TarefaEnum paraIntegracao);
}
