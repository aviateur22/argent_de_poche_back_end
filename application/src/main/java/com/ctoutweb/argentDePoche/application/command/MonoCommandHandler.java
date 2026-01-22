package com.ctoutweb.argentDePoche.application.command;
import reactor.core.publisher.Mono;

/**
 * Contrat que devra respecter l'implementation du handler
 *
 * @param <C> Type de l'objet attendu qui sera a persister
 * @param <R> Type de l'objet attendu en réponse;
 */
public interface MonoCommandHandler<C extends Command<R>, R> {
    Mono<R> handle(C command);

}
