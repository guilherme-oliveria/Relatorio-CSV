package br.jus.tjro.gabinete.listener.kafka.consumers;

import br.jus.tjro.gabinete.listener.kafka.core.MessageListenerAbstract;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.transiente.ProcessoParteExpedienteTransiente;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.scheduled.BuscaExpedientesDoProcesso;
import br.jus.tjro.gabinete.service.local.ProcessoParteExpedienteService;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BuscaProcessoParteExpedienteKafkaListener extends MessageListenerAbstract<ProcessoParteExpedienteTransiente> {

    private final ProcessoParteExpedienteService service;
    private final ProcessosRepository repository;
    public static final String TOPIC_NAME =  "GABINETE_BUSCA_PROCESSO_PARTE_EXPEDIENTE";
    private final Logger logger = LoggerFactory.getLogger(BuscaProcessoParteExpedienteKafkaListener.class);


    @Autowired
    public BuscaProcessoParteExpedienteKafkaListener(ProcessoParteExpedienteService service,
                                                     ProcessosRepository repository, KafkaProducerService kafkaProducer) {
        super(TOPIC_NAME, kafkaProducer, ProcessoParteExpedienteTransiente.class,false);
        this.repository = repository;
        this.service = service;
    }

    @Override
    public void executa(ProcessoParteExpedienteTransiente ppe) throws Exception {
        try {
            Processo processo = repository.findByIdProcessoSistemaLegadoAndSistema(ppe.getIdProcessoSistemalegado(),ppe.getSistema()).orElseThrow();
            if(processo == null)
                throw new Exception("Não Existe Processo IdSistemaLegado "+ppe.getIdProcessoSistemalegado()+" Sistema "+ppe.getSistema());
            service.importaOuAtualizaProcessoParteExpediente(ppe,processo);
            getLogger().info("Finalizada a execução da importacao do processo parte expediente idLegado " + ppe.getIdLegado());
        } catch (Exception e) {
            throw new Exception("Erro ao processar mensagem de importacao do processo parte expediente idLegado " + ppe.getIdLegado(), e);
        }
    }

    @Override
    protected Logger getLogger() {
        return this.logger;
    }

}
