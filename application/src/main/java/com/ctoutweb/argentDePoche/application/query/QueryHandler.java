package com.ctoutweb.argentDePoche.application.query;

import org.reactivestreams.Publisher;

/**
 * Contrat permettant de récupérer de la données
 *
 * @param <Q> Dto envoyé a la commande nécessaire a la récupération des données
 * @param <R> Type de l'objet renvoyé qui est attendu en retour
 */
public interface QueryHandler<Q extends Query<R>, R> {
    Publisher<R> handle(Q queryDto);
}
