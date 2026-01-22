package com.ctoutweb.argentDePoche.application.configuration.bus;

import java.util.function.Consumer;

public interface EventBus {
    /**
     * Publication d'un evenement vers les suscribers
     *
     * @param event L'évenement à publier
     */
    <T> void publish(T event);

    /**
     * Souscribtion d'un message avec un type données
     *
     * @param messageType - La class de message a recevoir
     * @param handler - Consumer qui gerera le message
     * @param <T> - message type
     */
    <T> void subscribe(Class<T> messageType, Consumer<T> handler);
}
