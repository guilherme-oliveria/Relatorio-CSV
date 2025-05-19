package br.jus.tjro.gabinete.listener.kafka.core;

import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.kafka.listener.ContainerProperties.AckMode.MANUAL_IMMEDIATE;


@Component
public class kafkaComsumerRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(kafkaComsumerRunner.class);
    private final int concurrency;
    private final KafkaTransactionManager transactionManager;

    @Autowired
    public kafkaComsumerRunner(@Value("${KAFKA_ENABLE_RUNNER:true}") boolean runnerAtivo,
                               @Value("${KAFKA_RUNNER_SINGLE_TOPIC:nada}") List<String> topics,
                               @Qualifier("consumerConfigs") HashMap<String, Object> consumerConfig,
                               List<MessageListenerInterface<?>> listeners,
                               @Value("${concurrency:5}") int concurrency,
                               KafkaProducerService producerService) {
        this.concurrency = concurrency;
        this.transactionManager = producerService.getKtm();
        List<MessageListenerInterface<?>> listenersSelected = listeners.stream()
            .filter(p -> runnerAtivo || topics.stream().anyMatch(topic -> p.getTopics().equals(topic)))
            .toList();

        LOGGER.info("Iniciando construção dos runners kafka");
        for (MessageListenerInterface<?> listener : listenersSelected) {
            String topico = listener.getTopics();
            createContainerAsync(consumerConfig, topico, listener);
            LOGGER.info("Finalizando construção dos container kafka");
        }
    }

    private void createContainerAsync(Map<String, Object> consumerConfig, String topico, MessageListenerInterface<?> listener) {
        ContainerProperties containerProps = new ContainerProperties(topico);
        containerProps.setAckMode(MANUAL_IMMEDIATE);
        containerProps.setMessageListener(listener);
        if(transactionManager != null)
            containerProps.setTransactionManager(transactionManager);
        consumerConfig.put(JsonDeserializer.VALUE_DEFAULT_TYPE,listener.getType());
        DefaultKafkaConsumerFactory consumerFactory = new DefaultKafkaConsumerFactory<>(consumerConfig);
        ConcurrentMessageListenerContainer container = new ConcurrentMessageListenerContainer<>(consumerFactory, containerProps);
        container.setConcurrency(concurrency);
        container.start();
    }
}
