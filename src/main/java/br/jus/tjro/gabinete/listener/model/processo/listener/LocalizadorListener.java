package br.jus.tjro.gabinete.listener.model.processo.listener;


import br.jus.tjro.gabinete.listener.model.processo.events.*;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoTag;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LocalizadorListener {

    private final Logger logger = LoggerFactory.getLogger(LocalizadorListener.class);
    private final KafkaProducerService producerService;

    @Autowired
    public LocalizadorListener(KafkaProducerService producerService) {
        this.producerService = producerService;
    }

    @EventListener
    public void atualizaLocalizadorAocriarOuAtualizarProcesso(ProcessoCreateOrUpdateEvent event) {
        try {
            send(event.getProcesso());
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }


    @EventListener
    public void atualizaLocalizadorAoAtualizarProcessoTag(ProcessoTagEvent event) {
        try {
            send(event.getProcessoTag().getProcesso());
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }

    public void send(Processo p){
        String topico = "GABINETE_LOCALIZADOR_PROCESSO_CREATE_UPDATE";
//        logger.info("Enviando mensagem ao kafka no topico: " + topico);
        producerService.send(topico, p);
    }

    public void send(Iterable<Processo> retorno) {
        retorno.forEach(this::send);
    }

    public void send(List<ProcessoTag> processoTags) {
        processoTags.stream().map(ProcessoTag::getProcesso).collect(Collectors.toSet()).forEach(this::send);
    }
}

