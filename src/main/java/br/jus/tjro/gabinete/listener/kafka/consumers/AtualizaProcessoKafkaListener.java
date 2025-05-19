package br.jus.tjro.gabinete.listener.kafka.consumers;

import br.jus.tjro.gabinete.listener.kafka.core.MessageListenerAbstract;
import br.jus.tjro.gabinete.listener.model.processo.listener.LocalizadorListener;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.enums.TipoNotificacaoEnum;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.WrapperAtualizaProcesso;
import br.jus.tjro.gabinete.model.gab.transiente.Notificacao;
import br.jus.tjro.gabinete.scheduled.BuscaProcessosConclusos;
import br.jus.tjro.gabinete.service.local.ProcessoService;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import br.jus.tjro.gabinete.service.websocket.WebSocketTransportService;
import br.jus.tjro.gabinete.util.AuthenticationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;

import java.util.Date;

@Component
@Profile("!test")
public class AtualizaProcessoKafkaListener extends MessageListenerAbstract<WrapperAtualizaProcesso> {

    private final WebSocketTransportService webSocket;
    private final BuscaProcessosConclusos buscaProcessosConclusos;
    private final ProcessoService service;
    public static final String TOPIC_NAME =  "GABINETE_ATUALIZACAO_PROCESSO";
    private final Logger logger = LoggerFactory.getLogger(AtualizaProcessoKafkaListener.class);
    private final LocalizadorListener localizadorListener;

    @Autowired
    public AtualizaProcessoKafkaListener(ProcessoService service,
                                        BuscaProcessosConclusos buscaProcessosConclusos,
                                        KafkaProducerService kafkaProducer, WebSocketTransportService webSocket, 
                                        LocalizadorListener localizadorListener) {
        super(TOPIC_NAME, kafkaProducer, WrapperAtualizaProcesso.class,false);
        this.service = service;
        this.webSocket = webSocket;
        this.buscaProcessosConclusos = buscaProcessosConclusos;
        this.localizadorListener = localizadorListener;
    }

    @Override
    public void executa(WrapperAtualizaProcesso wrapperAtualizaProcesso) throws Exception {
        try {
            Thread.sleep(2000);
            Usuario usuariLogado = AuthenticationUtil.setAuthenticationAdminInContext();
            Processo processo = this.service.findOne(wrapperAtualizaProcesso.getIdProcesso());
            logger.info("Iniciando a atualização manual do processo com kafka. Processo numero: "+processo.getNumeroProcesso());
            Processo processoUpdated = service.atualizaProcessoManualmente(processo, usuariLogado);
            webSocket.send(wrapperAtualizaProcesso.getUsuario(), getNotificacao(processo));
            logger.info("Finalizando atualização manual do processo: "+processo.getNumeroProcesso());
            localizadorListener.send(processoUpdated);
        } catch (Exception e) {
            System.out.println("Processo: " + wrapperAtualizaProcesso.getIdProcesso().toString());
            throw new Exception("Erro ao processar mensagem.", e);
        }
    }

    private Notificacao getNotificacao(Processo processo) {
        return new Notificacao(TipoNotificacaoEnum.PROCESSO_ATUALIZADO,processo, new Date());
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

}
