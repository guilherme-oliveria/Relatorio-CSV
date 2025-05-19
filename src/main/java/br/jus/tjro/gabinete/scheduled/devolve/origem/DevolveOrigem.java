package br.jus.tjro.gabinete.scheduled.devolve.origem;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.listener.model.processo.listener.LocalizadorListener;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.scheduled.devolve.origem.tarefasdevolucoes.TarefaDevolucao;
import br.jus.tjro.gabinete.service.local.*;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static io.vavr.collection.Stream.ofAll;
import static java.lang.String.format;

@Service
public class DevolveOrigem {

    public static final String TOPIC_DEVOLVE_ORIGEM = "GABINETE_DEVOLVE_ORIGEM";

    final ProcessoService processoService;
    final ProcessosRepository processosRepository;
    final MinutaService documentoService;

    final MinutaMovimentoService minutaMovimentoService;

    final MinutaMovimentoComplementoService minutaMovimentoComplementoService;

    final KafkaProducerService kafkaProducerService;

    final List<TarefaDevolucao> tarefasDevolucoes;

    private final Logger logger = LoggerFactory.getLogger(DevolveOrigem.class);
    private final LocalizadorListener localizadorListener;
    private boolean devolveAutomatico = false;

    @Autowired
    public DevolveOrigem(
        //Quando servidor publico é maior que 0 a integração fica lento. Thread.sleep(servidorPublico (segundos))
        @Value("${scheduled.devolve.origem.servidor.publico:0}") Long servidorPublico,
        @Value("${scheduled.devolveParaIntegracaoAutomatico:false}") boolean devolveAutomatico,
        LocalizadorListener localizadorListener, List<TarefaDevolucao> tarefasDevolucoes,
        ProcessoService processoService,
        MinutaService documentoService,
        MinutaMovimentoService minutaMovimentoService,
        MinutaMovimentoComplementoService minutaMovimentoComplementoService,
        KafkaProducerService kafkaProducerService,
        ProcessosRepository processosRepository) {
        this.localizadorListener = localizadorListener;
        this.tarefasDevolucoes = tarefasDevolucoes;
        this.processoService = processoService;
        this.documentoService = documentoService;
        this.minutaMovimentoService = minutaMovimentoService;
        this.minutaMovimentoComplementoService = minutaMovimentoComplementoService;
        this.kafkaProducerService = kafkaProducerService;
        this.devolveAutomatico = devolveAutomatico;
        this.processosRepository = processosRepository;
    }

    public boolean devolveOrigem(Processo processo) throws Exception{
        Optional<Processo> processoDevolvido = Optional.empty();

        for (TarefaDevolucao td : this.tarefasDevolucoes) {
            if (td.getTarefas().contains(processo.getTarefa())) {
                try {
                    processoDevolvido = Optional.ofNullable(td.devolverOrigem(processo));
                } catch (Exception e) {
                    logger.error(format("Processo %s com erro de devolução para a tarefa %s:", processo, processo.getTarefa().toString()));
                    logger.error(e.getMessage());
                    throw new Exception(e);
                }
            }
        }

        processoDevolvido.ifPresent(p -> logger.info("Processo Devolvido para Origem " + p.getNumeroProcesso()));
        localizadorListener.send(processoDevolvido.orElse(processo));

        return processoDevolvido.isPresent();
    }

    public List<TarefaEnum> getTarefasDisponiveis() {
        return this.tarefasDevolucoes.stream()
            .flatMap(d -> d.getTarefas().stream())
            .toList();
    }

    @Scheduled(fixedDelay = 600000)
    public void devolveParaIntegracao() throws Exception {
        if (devolveAutomatico) {
            List<Processo> processos = processosRepository.findByTarefaEnum(TarefaEnum.ParaIntegracao);
            boolean resultadoDevolucaoOrigem = true;
            for(Processo processo : processos) {
                try {
                    Optional<Processo> processoEntity = processosRepository.findOptionalById(processo.getId());
                    resultadoDevolucaoOrigem = this.devolveOrigem(processoEntity.get());
                    if(!resultadoDevolucaoOrigem) {
                        processoEntity.get().setTarefa(TarefaEnum.ParaIntegracaoComErro);
                        processosRepository.save(processoEntity.get());
                    }
                } catch (Exception e) {
                    processo.setTarefa(TarefaEnum.ParaIntegracaoComErro);
                    processosRepository.save(processo);
                }
            }
        }
    }
}
