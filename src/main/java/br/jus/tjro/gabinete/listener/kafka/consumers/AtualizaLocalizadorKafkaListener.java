package br.jus.tjro.gabinete.listener.kafka.consumers;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.listener.kafka.core.MessageListenerAbstract;
import br.jus.tjro.gabinete.listener.model.processo.listener.LocalizadorListener;
import br.jus.tjro.gabinete.model.gab.localizador.LocalizadorCaixa;
import br.jus.tjro.gabinete.model.gab.processo.WrapperAtualizaProcesso;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.service.remoto.LocalizadorRemotoService;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("!test")
public class AtualizaLocalizadorKafkaListener extends MessageListenerAbstract<LocalizadorCaixa> {

    private final Logger logger = LoggerFactory.getLogger(AtualizaProcessoKafkaListener.class);

    public static final String TOPIC_NAME = "GABINETE_ATUALIZACAO_LOCALIZADOR";
    private final ProcessosRepository processosRepository;
    private final LocalizadorRemotoService localizadorRemotoService;
    private final LocalizadorListener localizadorListener;

    @Autowired
    public AtualizaLocalizadorKafkaListener(
        ProcessosRepository processosRepository,
        LocalizadorRemotoService localizadorRemotoService,
        LocalizadorListener localizadorListener,
        KafkaProducerService kafkaProducer) {

        super(TOPIC_NAME, kafkaProducer, LocalizadorCaixa.class,false);

        this.processosRepository = processosRepository;
        this.localizadorRemotoService = localizadorRemotoService;
        this.localizadorListener = localizadorListener;
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    public void executa(LocalizadorCaixa localizador) throws Exception {
        List<Long> processosLocalizados = localizadorRemotoService.buscaProcessoLocalizador(localizador);

        processosRepository.findAllById(processosLocalizados)
            .forEach(localizadorListener::send);
    }
}
