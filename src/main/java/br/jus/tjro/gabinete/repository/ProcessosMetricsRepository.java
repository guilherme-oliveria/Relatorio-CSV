package br.jus.tjro.gabinete.repository;

import br.jus.tjro.gabinete.infrastructure.metrics.ProcessoPorTarefa;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessosMetricsRepository {

    List<ProcessoPorTarefa> porTarefasEOrgaoJuldador();

    Long quantosProcessosComProblema();

    Long quantosProcessosPendentes();
}
