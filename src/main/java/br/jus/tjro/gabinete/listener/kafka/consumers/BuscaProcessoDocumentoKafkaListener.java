package br.jus.tjro.gabinete.listener.kafka.consumers;

import br.jus.tjro.gabinete.listener.kafka.core.MessageListenerAbstract;
import br.jus.tjro.gabinete.model.gab.ProcessoRequestPage;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.scheduled.BuscaDocumentosDosProcessosConclusos;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BuscaProcessoDocumentoKafkaListener extends MessageListenerAbstract<ProcessoRequestPage> {

    private final BuscaDocumentosDosProcessosConclusos service;
    private final ProcessosRepository repository;
    public static final String TOPIC_NAME =  "GABINETE_BUSCA_PROCESSO_DOCUMENTO";
    private final Logger logger = LoggerFactory.getLogger(BuscaProcessoDocumentoKafkaListener.class);


    @Autowired
    public BuscaProcessoDocumentoKafkaListener(BuscaDocumentosDosProcessosConclusos service,
                                               ProcessosRepository repository, KafkaProducerService kafkaProducer) {
        super(TOPIC_NAME, kafkaProducer, ProcessoRequestPage.class,false);
        this.repository = repository;
        this.service = service;
    }

    @Override
    public void executa(ProcessoRequestPage processoPage) throws Exception {
        try {
            Processo p = repository.findById(processoPage.getProcesso().getId()).orElseThrow(() -> new Exception("Erro ao buscar processo ao processar mensagem do kafka. "+processoPage.getProcesso().getId()));
            getLogger().info("Iniciando a execução da importacao dos documentos para o processo " + p.getNumeroProcesso());
            this.service.executaPorProcesso(p,processoPage.getNumber());
            getLogger().info("Finalizada a execução da importacao dos documentos para o processo " + p.getNumeroProcesso());
        } catch (Exception e) {
            throw new Exception("Erro ao processar mensagem de importacao dos documentos para o processo. "+processoPage.getProcesso().getId(), e);
        }
    }

    @Override
    protected Logger getLogger() {
        return this.logger;
    }

}
