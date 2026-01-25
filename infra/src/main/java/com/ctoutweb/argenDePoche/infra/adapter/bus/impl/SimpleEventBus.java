package com.ctoutweb.argenDePoche.infra.adapter.bus.impl;

import com.ctoutweb.argenDePoche.infra.exception.EventException;
import com.ctoutweb.argentDePoche.application.configuration.bus.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Component
public class SimpleEventBus implements EventBus {
    private static final Logger LOGGER = LogManager.getLogger();
    Map<Class<?>, List<Consumer<?>>> subscribers = new ConcurrentHashMap<>();

    @Override
    public <T> void publish(T event) {
        List<Consumer<?>> consumers = subscribers.get(event.getClass());
        if(consumers != null) {
            for(Consumer<?> c : consumers) {
                @SuppressWarnings("unchecked")
                Consumer<T> typedConsumer = (Consumer<T>) c;
                try {
                    typedConsumer.accept(event);
                } catch (Exception exception) {
                    LOGGER.error(eventExceptionMessage(event, typedConsumer, exception));
                    throw new EventException("Exception sur un evenement");
                }
            }
        }
    }

    @Override
    public <T> void subscribe(Class<T> messageType, Consumer<T> consumer) {
        this.subscribers.computeIfAbsent(messageType, k -> new ArrayList<>()).add(consumer);
    }

    private <T> String eventExceptionMessage(T event, Consumer<T> consumer, Exception exception) {
        return String.format("Evenement suivant en echec: %s  - exception: %s", event, exception);
    }
}
