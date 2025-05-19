package br.jus.tjro.gabinete.listener.kafka.core;

import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface MessageListenerInterface<T> extends AcknowledgingMessageListener<String, T> {

    String getTopics();

    Class<T> getType();

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void executa(T payload) throws Exception;
}
