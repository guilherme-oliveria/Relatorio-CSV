package br.jus.tjro.gabinete.listener.kafka.consumers;

import br.jus.tjro.gabinete.listener.kafka.core.MessageListenerAbstract;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.scheduled.devolve.origem.DevolveOrigem;
import br.jus.tjro.gabinete.service.local.AssinaturaService;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static br.jus.tjro.gabinete.scheduled.devolve.origem.DevolveOrigem.TOPIC_DEVOLVE_ORIGEM;

@Component
@ConditionalOnProperty(prefix = "kafka.devolve", name = "consumer", havingValue = "true")
public class DevolveOrigemKafkaListener extends MessageListenerAbstract<Processo> {
    public static final String TOPIC_NAME = TOPIC_DEVOLVE_ORIGEM;
    private final Logger looger = LoggerFactory.getLogger(DevolveOrigemKafkaListener.class);
    private final ProcessosRepository processosRepository;
    private final DevolveOrigem devolveOrigem;

    @Autowired
    public DevolveOrigemKafkaListener(KafkaProducerService kafkaProducer, ProcessosRepository processosRepository, DevolveOrigem devolveOrigem) {
        super(TOPIC_NAME, kafkaProducer, Processo.class, false);
        this.processosRepository = processosRepository;
        this.devolveOrigem = devolveOrigem;
    }

    @Override
    protected Logger getLogger() {
        return looger;
    }

    @Override
    public void executa(Processo processo) throws Exception {
        Optional<Processo> processoEntity = processosRepository.findOptionalById(processo.getId());
        boolean resultadoDevolucaoOrigem = true;
        if(processoEntity.isPresent())
            resultadoDevolucaoOrigem = devolveOrigem.devolveOrigem(processoEntity.get());
    }
}
