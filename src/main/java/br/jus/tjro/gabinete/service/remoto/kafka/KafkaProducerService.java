package br.jus.tjro.gabinete.service.remoto.kafka;


import com.google.common.collect.ImmutableCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class KafkaProducerService {

    private final KafkaTemplate kafkaObj;
    private final Boolean enable;

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    private final KafkaTransactionManager ktm;

    @Autowired
    public KafkaProducerService(KafkaTemplate kafkaObj,
                                @Value("${:true}") Boolean enable, KafkaTransactionManager ktm) {
        this.kafkaObj = kafkaObj;
        this.enable = enable;
        this.ktm = ktm;
    }

    @Transactional
    public CompletableFuture<SendResult<Integer, String>> send(String topic, Object data) {
        if(data instanceof ImmutableCollection){
            data = new ArrayList<>(((ImmutableCollection) data));
        }else if(data instanceof List){
            data = new ArrayList<>(((List) data));
        }
        if(!enable)
            return null;
        logger.info("Enviando mensagem ao topico "+topic);
        return kafkaObj.send(topic, data);
    }

    public KafkaTemplate getKafkaObj() {
        return kafkaObj;
    }

    public KafkaTransactionManager getKtm() {
        return ktm;
    }
}
