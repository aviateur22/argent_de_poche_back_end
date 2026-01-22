package com.ctoutweb.argentDePoche.application.configuration.bus;


import com.ctoutweb.argentDePoche.application.query.Query;
import com.ctoutweb.argentDePoche.application.query.QueryHandler;
import org.reactivestreams.Publisher;

public interface QueryBus {
    /**
     * Enregistrement d'un QuerydHanler pour un query specific
     *
     * @param queryType La class du QueryHandler
     * @param handler Le query handler
     * @param <Q> Le type de la Query
     * @param <R> Le type renvoyé par la command
     */
    <Q extends Query<R>, R> void registerHandler(Class<Q> queryType, QueryHandler<Q, R> handler);

    /**
     * Execution d'une commande
     *
     * @param query Le type de query a executer
     *
     * @param <Q> Le type de la Query
     * @param <R> Le type renvoyer par la query
     *
     * @return Le type de resultat renvoyer par la qyery
     */
    <Q  extends Query<R>, R> Publisher<R> executeQuery(Q query);
}
