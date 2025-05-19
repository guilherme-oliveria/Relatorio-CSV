package br.jus.tjro.gabinete.scheduled.devolve.origem.tarefasdevolucoes;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.listener.kafka.consumers.PagamentosAlvaraKafkaListener;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import org.springframework.stereotype.Component;

import java.util.List;

import static br.jus.tjro.gabinete.Tarefas.core.TarefaEnum.ParaIntegracaoAlvara;
import static java.util.List.of;

@Component
public class ParaIntegracaoAlvaraDevolucao implements TarefaDevolucao {

    private final KafkaProducerService kafkaProducerService;

    public ParaIntegracaoAlvaraDevolucao(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public List<TarefaEnum> getTarefas() {
        return of(ParaIntegracaoAlvara);
    }

    @Override
    public Processo devolverOrigem(Processo processo) {
        kafkaProducerService.send(PagamentosAlvaraKafkaListener.TOPIC_NAME, processo.getId());
        return processo;
    }
}
