package br.jus.tjro.gabinete.repository;

import br.jus.tjro.gabinete.infrastructure.metrics.ProcessoPorTarefa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Repository
@ConditionalOnProperty(name="spring.datasource.platform", havingValue="oracle")
public class ProcessosMetricsOracleRepository implements ProcessosMetricsRepository {

    private final Logger log = LoggerFactory.getLogger(ProcessosMetricsOracleRepository.class);

    @PersistenceContext
    private EntityManager entityManager;

    public List<ProcessoPorTarefa> porTarefasEOrgaoJuldador() {
        log.info("TELEMETRIA: Capturando porTarefasEOrgaoJuldador");
        return this.entityManager.createQuery(
            "SELECT new br.jus.tjro.gabinete.infrastructure.metrics.ProcessoPorTarefa(p.tarefaEnum, p.orgaoJulgador, count(1)) " +
                "FROM Processo p " +
                "WHERE p.tarefaEnum IS NOT NULL AND p.orgaoJulgador IS NOT NULL " +
                "GROUP BY p.tarefaEnum, p.orgaoJulgador", ProcessoPorTarefa.class).getResultList();
    }

    public Long quantosProcessosComProblema() {
        log.info("TELEMETRIA: Capturando quantosProcessosComProblema");
        return this.entityManager.createQuery(
            "SELECT count(1) " +
                "FROM Processo p " +
                "WHERE p.erroSincronizacao <> 0 OR " +
                "(p.dataSincronizacao IS NULL AND p.tarefaEnum != 'NaoConcluso')", Long.class).getSingleResult();
    }

    public Long quantosProcessosPendentes() {
        log.info("TELEMETRIA: Capturando quantosProcessosPendentes");
        return this.entityManager.createQuery(
            "SELECT count(1) " +
                "FROM Processo p " +
                "WHERE (p.dataSincronizacao <= (sysdate - 1/24) OR p.dataSincronizacao IS NULL) AND " +
                "p.tarefaEnum != 'NaoConcluso'", Long.class).getSingleResult();
    }

}
