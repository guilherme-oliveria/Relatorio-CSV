package br.jus.tjro.gabinete.listener.kafka.consumers;

import br.jus.tjro.gabinete.listener.kafka.core.MessageListenerAbstract;
import br.jus.tjro.gabinete.listener.model.processo.listener.LocalizadorListener;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.service.local.MinutaTarefaLogService;
import br.jus.tjro.gabinete.service.remoto.alvara.PagamentosRemotoService;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import br.jus.tjro.gabinete.service.websocket.WebSocketTransportService;
import br.jus.tjro.gabinete.util.AuthenticationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PagamentosAlvaraKafkaListener extends MessageListenerAbstract<Long> {

    private final ProcessosRepository processosRepository;
    private final MinutaTarefaLogService minutaTarefaLogService;
    private final PagamentosRemotoService service;
    public static final String TOPIC_NAME =  "GABINETE_PAGAMENTOS_ALVARA";
    private final Logger logger = LoggerFactory.getLogger(PagamentosAlvaraKafkaListener.class);
    private final LocalizadorListener localizadorListener;

    @Autowired
    public PagamentosAlvaraKafkaListener(PagamentosRemotoService service,
                                         ProcessosRepository processosRepository,
                                         KafkaProducerService kafkaProducer, WebSocketTransportService webSocket,
                                         MinutaTarefaLogService minutaTarefaLogService, LocalizadorListener localizadorListener) {
        super(TOPIC_NAME, kafkaProducer, Long.class,false);
        this.service = service;
        this.processosRepository = processosRepository;
        this.minutaTarefaLogService = minutaTarefaLogService;
        this.localizadorListener = localizadorListener;
    }

    @Override
    public void executa(Long idProcesso) throws Exception {
        try {
            Usuario usuario = AuthenticationUtil.setAuthenticationAdminInContext();
            Optional<Processo> processo = processosRepository.findById(idProcesso);
            if(processo.isPresent()) {
                logger.info("Enviando alvara para pagamento. Numero Processo: "+processo.get().getNumeroProcesso());
                if(service.enviarAlvaraPagamento(processo.get())){
                    processo.get().naoConcluso();
                    minutaTarefaLogService.registrarLog(processo.get(),usuario);
                    Processo processoUpdated = processosRepository.save(processo.get());
                    localizadorListener.send(processoUpdated);
                    logger.info("Finalizando alvara para o processo: "+processo.get().getNumeroProcesso());
                }else{
                    logger.error("Erro ao processar alvara para pagamento. para o processo: "+processo.get().getNumeroProcesso());
                }
            }
        } catch (Exception e) {
            throw new Exception("Erro ao processar mensagem. para id processo: "+idProcesso, e);
        }
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

}
