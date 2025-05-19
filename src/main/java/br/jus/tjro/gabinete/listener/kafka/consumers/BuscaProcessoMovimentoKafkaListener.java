package br.jus.tjro.gabinete.listener.kafka.consumers;

import br.jus.tjro.gabinete.listener.kafka.core.MessageListenerAbstract;
import br.jus.tjro.gabinete.model.gab.ProcessoRequestPage;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.scheduled.BuscaDocumentosDosProcessosConclusos;
import br.jus.tjro.gabinete.scheduled.BuscaMovimentosDosProcessos;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

//@Component
public class BuscaProcessoMovimentoKafkaListener extends MessageListenerAbstract<Long> {

    private final BuscaMovimentosDosProcessos service;
    private final ProcessosRepository repository;
    public static final String TOPIC_NAME =  "GABINETE_BUSCA_PROCESSO_MOVIMENTO";
    private final Logger logger = LoggerFactory.getLogger(BuscaProcessoMovimentoKafkaListener.class);


    @Autowired
    public BuscaProcessoMovimentoKafkaListener(BuscaMovimentosDosProcessos service,
                                               ProcessosRepository repository, KafkaProducerService kafkaProducer) {
        super(TOPIC_NAME, kafkaProducer, Long.class,false);
        this.repository = repository;
        this.service = service;
    }

    @Override
    public void executa(Long idProcesso) throws Exception {
        try {
            Optional<Processo> processo = repository.findById(idProcesso);
            if(processo.isPresent()) {
                this.service.executaPorProcesso(processo.get());
                getLogger().info("Finalizada a execução da importacao do movimentos para o processo " + processo.get().getNumeroProcesso());
            }
        } catch (Exception e) {
            throw new Exception("Erro ao processar mensagem de mportacao do movimentos para o processo", e);
        }
    }

    @Override
    protected Logger getLogger() {
        return this.logger;
    }

}
