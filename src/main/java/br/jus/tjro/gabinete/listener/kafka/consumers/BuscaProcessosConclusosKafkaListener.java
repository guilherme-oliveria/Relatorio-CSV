package br.jus.tjro.gabinete.listener.kafka.consumers;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.listener.kafka.core.MessageListenerAbstract;
import br.jus.tjro.gabinete.listener.model.processo.listener.LocalizadorListener;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.transiente.ProcessoConcluso;
import br.jus.tjro.gabinete.scheduled.BuscaProcessosConclusos;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import br.jus.tjro.gabinete.util.AuthenticationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BuscaProcessosConclusosKafkaListener extends MessageListenerAbstract<ProcessoConcluso> {

    private final BuscaProcessosConclusos service;
    public static final String TOPIC_NAME =  "GABINETE_BUSCA_CONCLUSOS";
    private final Logger logger = LoggerFactory.getLogger(BuscaProcessosConclusosKafkaListener.class);
    private final LocalizadorListener localizadorListener;

    @Autowired
    public BuscaProcessosConclusosKafkaListener(BuscaProcessosConclusos service,
                                                KafkaProducerService kafkaProducer, LocalizadorListener localizadorListener) {
        super(TOPIC_NAME, kafkaProducer, ProcessoConcluso.class,false);
        this.service = service;
        this.localizadorListener = localizadorListener;
    }

    @Override
    public void executa(ProcessoConcluso proc) throws Exception {
        try {
            logger.info("Iniciando Busca conclusão de processo: " + proc.getNumeroProcesso());
            Usuario usuarioLogado = AuthenticationUtil.setAuthenticationAdminInContext();
            final Processo processo = this.service.buscaProcessosNoWebService(proc.getFonteDadosEnum(), proc, TarefaEnum.Minutar,usuarioLogado);
            localizadorListener.send(processo);
            logger.info("Finalizando Busca concluso de processo: " + processo.getId() + " -> " +proc.getNumeroProcesso());
        } catch (Exception e) {
            throw new Exception("Erro ao processar mensagem de localizadores", e);
        }
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

}
