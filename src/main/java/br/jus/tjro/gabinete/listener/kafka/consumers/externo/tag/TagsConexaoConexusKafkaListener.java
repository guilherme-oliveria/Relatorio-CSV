package br.jus.tjro.gabinete.listener.kafka.consumers.externo.tag;

import br.jus.tjro.gabinete.listener.kafka.consumers.externo.tag.util.KafkaTagExternoUtil;
import br.jus.tjro.gabinete.listener.kafka.core.MessageListenerAbstract;
import br.jus.tjro.gabinete.model.gab.processo.WrapperTagExterna;
import br.jus.tjro.gabinete.service.local.ProcessoService;
import br.jus.tjro.gabinete.service.local.ProcessoTagService;
import br.jus.tjro.gabinete.service.local.localizador.LocalizadorService;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@Profile("!test")
@ConditionalOnExpression("${KAFKA_ENABLE_RUNNER:true} && ${TAGS_CONEXUS_ATIVADO:true}")
public class TagsConexaoConexusKafkaListener extends MessageListenerAbstract<WrapperTagExterna> {

    private final ProcessoService processoService;
    private final ProcessoTagService processoTagService;
    private final LocalizadorService localizadorService;

    public static final String TOPIC_NAME =  "conexus-processos-analise-conexao-concluida";
    private final Logger logger = LoggerFactory.getLogger(TagsConexaoConexusKafkaListener.class);

    @Autowired
    public TagsConexaoConexusKafkaListener(ProcessoTagService processoTagService,
                                           ProcessoService processoService,
                                           LocalizadorService localizadorService,
                                           KafkaProducerService kafkaProducer) {
        super(TOPIC_NAME, kafkaProducer, WrapperTagExterna.class,false);
        this.processoTagService = processoTagService;
        this.processoService = processoService;
        this.localizadorService = localizadorService;
    }

    @KafkaListener(topics = TOPIC_NAME)
    public void executa(WrapperTagExterna wtc) throws Exception {
        KafkaTagExternoUtil.extrairTagElocalizadorParaProcesso(wtc, processoService, logger, processoTagService,
            localizadorService, "Conexus");
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

}
