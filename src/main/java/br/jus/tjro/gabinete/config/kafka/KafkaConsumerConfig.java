package br.jus.tjro.gabinete.config.kafka;

import java.util.HashMap;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;


@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    private final String groupId;
    private final String hosts;
    private final int session;
    private final int maxPollRecords;
    private final Boolean transaction;

    public KafkaConsumerConfig(@Value("${KAFKA_HOST}") String hosts,
                               @Value("${KAFKA_GROUP_ID:gabinete}") String groupId,
                               @Value("${KAFKA_SESSION_TIMEOUT_MS_CONFIG:180000}") int session,
                               @Value("${KAFKA_MAX_POLL_RECORDS:5}") int maxPollRecords,
                               @Value("${KAFKA_TRANSACTION:false}") Boolean transaction) {
        this.hosts = hosts;
        this.groupId = groupId;
        this.session = session;
        this.maxPollRecords = maxPollRecords;
        this.transaction = transaction;
    }

    @Bean(name = "consumerConfigs")
    public HashMap<String, Object> consumerConfigs() {
        HashMap<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, hosts);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES,"*");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,session);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, session);
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, session/3);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        if(transaction)
            props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        return props;
    }

    @Bean
    public DefaultKafkaConsumerFactory defaultKafkaConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }
}
