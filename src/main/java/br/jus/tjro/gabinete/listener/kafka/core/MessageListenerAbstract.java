package br.jus.tjro.gabinete.listener.kafka.core;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.SendResult;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;


import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import br.jus.tjro.gabinete.util.KafkaHandlingUtil;


public abstract class MessageListenerAbstract<T> implements MessageListenerInterface<T> {

    private final String topics;
    private final Class<T> type;
    protected KafkaProducerService kafkaProducer;
    private final boolean espera;

    protected abstract Logger getLogger();

    public MessageListenerAbstract(String topics, KafkaProducerService kafkaProducer,
                                   Class<T> type, boolean espera) {
        this.topics = topics;
        this.type = type;
        this.kafkaProducer = kafkaProducer;
        this.espera = espera;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMessage(ConsumerRecord<String, T> message, Acknowledgment acknowledgment) {
        try {
            if(espera){
                getLogger().info("Iniciando Thread.sleep para o topic kafka: " + message.topic());
                KafkaHandlingUtil.espera(message.topic());
            }
            this.executa(message.value());
        }catch(InvalidDefinitionException e){
            publishToNextTopicOnError(message, acknowledgment);
            RuntimeException ex = new RuntimeException("Erro ao fazer parse da mensagem do kafka: " + message, e);
            getLogger().error(e.getMessage(),ex);
            throw ex;
        }
        catch (Exception e) {
            publishToNextTopicOnError(message, acknowledgment);
            RuntimeException ex = new RuntimeException("Erro ao executar mensagem do kafka: " + message, e);
            getLogger().error(e.getMessage(),ex);
            throw ex;
        }
        getLogger().info("Commitando a mensagem do - partição "+ message.partition() + " offset: " + message.offset() );
        commit(acknowledgment);
    }

    private void commit(Acknowledgment acknowledgment) {
        acknowledgment.acknowledge();
    }

    private void publishToNextTopicOnError(ConsumerRecord<String, T> message, Acknowledgment acknowledgment) {
        String topico = KafkaHandlingUtil.procuraProximoTopico(message.topic());
        if (topico.equals("")) {
            commit(acknowledgment);
            return;
        }
        CompletableFuture<SendResult<Integer, String>> future = kafkaProducer.send(topico, message.value());
        future.whenComplete((retorno, throwable) -> {
            commit(acknowledgment);
            getLogger().info("A mensagem " + retorno.getRecordMetadata().offset() + " do kafka não foi processada corretamente"
                + ". Enviando para a proxima fila do topico: " + topico + "");
        });
    }

    @Override
    public String getTopics() {
        return topics;
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    private <T> T jsonDeserializer(String json) throws IOException {
        return new ObjectMapper().reader().forType(getType()).readValue(json);
    }

}
