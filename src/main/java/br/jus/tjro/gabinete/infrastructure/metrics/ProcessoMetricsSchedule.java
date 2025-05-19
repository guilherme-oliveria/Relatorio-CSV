package br.jus.tjro.gabinete.infrastructure.metrics;

import br.jus.tjro.gabinete.repository.ProcessosMetricsRepository;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class ProcessoMetricsSchedule {
    private static final int ONE_MINUTE = 60000;
    private final ProcessosMetricsRepository repository;
    private final boolean ativo;

    public ProcessoMetricsSchedule(@Value("${metrics_enable:false}") boolean ativo, ProcessosMetricsRepository repository) {
        this.repository = repository;
        this.ativo = ativo;
    }

//    @Scheduled(fixedDelay = 10 * ONE_MINUTE)
    public void porTarefasEOrgaoJulgador() {
        if(ativo) {
            final List<ProcessoPorTarefa> processoPorTarefas = repository.porTarefasEOrgaoJuldador();
//            processoPorTarefas
//                .forEach(n ->
//                    counter("processos_tarefas", n.tags()).increment(n.getQuantidade())
//                );
        }
    }

//    @Scheduled(fixedDelay = 10 * ONE_MINUTE)
    public void quantidadeDeProcessosComProblema() {
        if(ativo) {
            final Long numeroDeProcesso = repository.quantosProcessosComProblema();
//            counter("processos_problemas").increment(numeroDeProcesso);
        }
    }

//    @Scheduled(fixedDelay = 10 * ONE_MINUTE)
    public void quantidadeDeProcessosPendentes() {
        if(ativo){
            final Long numeroDeProcesso = repository.quantosProcessosPendentes();
//            counter("processos_pendentes").increment(numeroDeProcesso);
        }
    }

}
