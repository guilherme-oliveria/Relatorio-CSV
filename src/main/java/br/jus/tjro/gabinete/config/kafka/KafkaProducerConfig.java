package br.jus.tjro.gabinete.config.kafka;

import java.util.HashMap;

import java.util.Map;

import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;

@Configuration
public class KafkaProducerConfig {

    private final String hosts;
    private final Boolean transaction;
    private final Boolean enableProducer;

    public KafkaProducerConfig(@Value("${KAFKA_HOST}") String hosts,
                               @Value("${KAFKA_TRANSACTION:false}") Boolean transaction,
                               @Value("${KAFKA_ENABLE_PRODUCER:true}") Boolean enableProducer) {
        this.hosts = hosts;
        this.transaction = transaction;
        this.enableProducer = enableProducer;
    }


    @Bean
    public DefaultKafkaProducerFactory producerObjFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, hosts);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 5000000); //5MB
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, true);
        DefaultKafkaProducerFactory producerObjFactory = new DefaultKafkaProducerFactory<>(props);
        if(transaction)
            producerObjFactory.setTransactionIdPrefix("windson");
        return producerObjFactory;
    }

    @Bean
    public KafkaProducerService kafkaTransactionManager(DefaultKafkaProducerFactory defaultKafkaProducerFactory) {
        KafkaTemplate<String, Object> template;
        KafkaTransactionManager ktm = null;
        if(transaction) {
            ktm = new KafkaTransactionManager(defaultKafkaProducerFactory);
            ktm.setNestedTransactionAllowed(transaction);
            ktm.setTransactionSynchronization(AbstractPlatformTransactionManager.SYNCHRONIZATION_ALWAYS);
            template = new KafkaTemplate<String, Object>(ktm.getProducerFactory());
        }else{
            template = new KafkaTemplate<String, Object>(defaultKafkaProducerFactory);
        }
        return new KafkaProducerService(template,enableProducer,ktm);
    }

    @Bean
    @Primary
    public JpaTransactionManager transactionManager(EntityManagerFactory em) {
        return new JpaTransactionManager(em);
    }
}
