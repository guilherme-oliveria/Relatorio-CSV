package br.jus.tjro.gabinete.scheduled.devolve.origem.tarefasdevolucoes;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.listener.kafka.consumers.IntegracaoPautaKafkaListener;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import org.springframework.stereotype.Component;

import java.util.List;

import static br.jus.tjro.gabinete.Tarefas.core.TarefaEnum.ParaIntegracaoPauta;
import static java.util.List.of;

@Component
public class ParaIntegracaoPautaDevolucao implements TarefaDevolucao {

    private final KafkaProducerService kafkaProducerService;

    public ParaIntegracaoPautaDevolucao(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public List<TarefaEnum> getTarefas() {
        return of(ParaIntegracaoPauta);
    }

    @Override
    public Processo devolverOrigem(Processo processo) {
        kafkaProducerService.send(IntegracaoPautaKafkaListener.TOPIC_NAME,processo.getId());
        return processo;
    }
}
