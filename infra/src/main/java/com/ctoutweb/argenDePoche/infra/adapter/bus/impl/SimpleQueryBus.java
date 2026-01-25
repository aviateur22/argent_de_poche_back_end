package com.ctoutweb.argenDePoche.infra.adapter.bus.impl;

import com.ctoutweb.argentDePoche.application.configuration.bus.QueryBus;
import com.ctoutweb.argentDePoche.application.query.Query;
import com.ctoutweb.argentDePoche.application.query.MonoQueryHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
public class SimpleQueryBus implements QueryBus {
    private final Map<Class<?>, MonoQueryHandler<?, ?>> queryHandlers = new HashMap<>();

    @Override
    public <Q extends Query<R>, R> void registerMonoHandler(Class<Q> queryType, MonoQueryHandler<Q, R> handler) {
        queryHandlers.put(queryType, handler);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Q extends Query<R>, R> Mono<R> executeQuery(Q query) {
        MonoQueryHandler<Q, R> handler = (MonoQueryHandler<Q, R>) queryHandlers.get(query.getClass());
        if (handler == null) {
            throw new RuntimeException("Pas de handler enregistré pour : " + query.getClass());
        }
        return handler.handle(query);
    }
}
