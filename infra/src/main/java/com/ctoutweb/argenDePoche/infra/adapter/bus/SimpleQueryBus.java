package com.ctoutweb.argenDePoche.infra.adapter.bus;

import com.ctoutweb.argentDePoche.application.configuration.bus.QueryBus;
import com.ctoutweb.argentDePoche.application.query.Query;
import com.ctoutweb.argentDePoche.application.query.QueryHandler;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SimpleQueryBus implements QueryBus {
    private final Map<Class<?>, QueryHandler<?, ?>> queryHandlers = new HashMap<>();

    @Override
    public <Q extends Query<R>, R> void registerHandler(Class<Q> queryType, QueryHandler<Q, R> handler) {
        queryHandlers.put(queryType, handler);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Q extends Query<R>, R> Publisher<R> executeQuery(Q query) {
        QueryHandler<Q, R> handler = (QueryHandler<Q, R>) queryHandlers.get(query.getClass());
        if (handler == null) {
            throw new RuntimeException("Pas de handler enregistré pour : " + query.getClass());
        }
        return handler.handle(query);
    }
}
