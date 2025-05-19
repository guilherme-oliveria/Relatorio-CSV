package br.jus.tjro.gabinete.listener.kafka.consumers;

import br.jus.tjro.gabinete.listener.kafka.core.MessageListenerAbstract;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoTag;
import br.jus.tjro.gabinete.model.gab.tag.Tag;
import br.jus.tjro.gabinete.repository.gab.minuta.MinutasRepository;
import br.jus.tjro.gabinete.service.local.ProcessoTagService;
import br.jus.tjro.gabinete.service.local.TagService;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PautaIntegradaKafkaListener extends MessageListenerAbstract<Long> {

    public static final String TOPIC_NAME = "GABINETE_PAUTA_INTEGRADA";
    private final MinutasRepository minutaRepository;
    private final TagService tagService;
    private final ProcessoTagService processoTagService;
    private final Logger logger = LoggerFactory.getLogger(PautaIntegradaKafkaListener.class);

    @Autowired
    public PautaIntegradaKafkaListener(MinutasRepository minutaRepository,
                                       TagService tagService,
                                       ProcessoTagService processoTagService,
                                       KafkaProducerService kafkaProducer) {
        super(TOPIC_NAME, kafkaProducer, Long.class, false);
        this.minutaRepository = minutaRepository;
        this.tagService = tagService;
        this.processoTagService = processoTagService;
    }

    @Override
    public void executa(Long idMinuta) throws Exception {
        minutaRepository.findById(idMinuta).ifPresent(minuta -> {
            try {
                logger.info("Recebido confirmação de pauta integrada. id minuta: " + idMinuta);
                Tag emSessaoTag = tagService.verificarSeExisteESalvar("EM_SESSAO", Tag.OrgaoTagTipoSistema);
                processoTagService.save(new ProcessoTag(emSessaoTag, true, minuta.getProcesso()));
            } catch (Exception e) {
                logger.error("Erro ao processar pauta integrada para id minuta: " + idMinuta, e);
            }
        });
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
