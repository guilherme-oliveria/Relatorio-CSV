package br.jus.tjro.gabinete.listener.kafka.consumers;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.listener.kafka.core.MessageListenerAbstract;
import br.jus.tjro.gabinete.listener.model.processo.listener.LocalizadorListener;
import br.jus.tjro.gabinete.model.gab.processo.WrapperBuscaProcesso;
import br.jus.tjro.gabinete.model.gab.enums.TipoNotificacaoEnum;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.transiente.Notificacao;
import br.jus.tjro.gabinete.model.gab.transiente.ProcessoConcluso;
import br.jus.tjro.gabinete.scheduled.BuscaProcessosConclusos;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import br.jus.tjro.gabinete.service.websocket.WebSocketTransportService;
import br.jus.tjro.gabinete.util.AuthenticationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class BuscaProcessosKafkaListener extends MessageListenerAbstract<WrapperBuscaProcesso> {

    private final WebSocketTransportService webSocket;
    private final BuscaProcessosConclusos service;
    public static final String TOPIC_NAME =  "GABINETE_BUSCA_PROCESSOS";
    private final Logger logger = LoggerFactory.getLogger(BuscaProcessosKafkaListener.class);
    private LocalizadorListener localizadorListener;

    @Autowired
    public BuscaProcessosKafkaListener(BuscaProcessosConclusos service,
                                       KafkaProducerService kafkaProducer, WebSocketTransportService webSocket, LocalizadorListener localizadorListener) {
        super(TOPIC_NAME, kafkaProducer, WrapperBuscaProcesso.class,false);
        this.service = service;
        this.webSocket = webSocket;
        this.localizadorListener = localizadorListener;
    }

    @Override
    public void executa(WrapperBuscaProcesso wrapper) throws Exception {
        try {
            ProcessoConcluso proc = wrapper.getProcessoConcluso();
            logger.info("Iniciando importação do processo com kafka. Processo numero: "+proc.getNumeroProcesso());
            Processo processo = this.service.buscaProcessosNoWebService(proc.getFonteDadosEnum(),proc, TarefaEnum.NaoConcluso,AuthenticationUtil.setAuthenticationAdminInContext());
            webSocket.send(wrapper.getUsuario(), getNotificacao(processo));
            localizadorListener.send(processo);
            logger.info("Finalizando importação do processo: "+proc.getNumeroProcesso());
        } catch (Exception e) {
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
