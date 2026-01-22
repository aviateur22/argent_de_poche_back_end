package com.ctoutweb.argenDePoche.infra.adapter.bus;

import com.ctoutweb.argentDePoche.application.command.ManyCommandHandler;
import com.ctoutweb.argentDePoche.application.configuration.bus.CommandBus;
import com.ctoutweb.argentDePoche.application.command.Command;
import com.ctoutweb.argentDePoche.application.command.MonoCommandHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation d'un bus de command
 */
@Component
public class SimpleCommandBus implements CommandBus {
    private final Map<Class<?>, MonoCommandHandler<?, ?>> handlers = new HashMap<>();
    private final Map<Class<?>, ManyCommandHandler<?, ?>> manyHandlers = new HashMap<>();

    // Enregistrement d'un handler
    @Override
    public <C extends Command<R>, R> void registerMonoHandler(Class<C> commandType, MonoCommandHandler<C, R> handler) {
        handlers.put(commandType, handler);
    }

    @Override
    public <C extends Command<R>, R> void registerManyHandler(Class<C> commandType, ManyCommandHandler<C, R> handler) {
        manyHandlers.put(commandType, handler);
    }


    // Exécution d'une commande
    @Override
    @SuppressWarnings("unchecked")
    public <C extends Command<R>, R> Mono<R> executeCommand(C command) {
        MonoCommandHandler<C, R> handler = (MonoCommandHandler<C, R>) handlers.get(command.getClass());
        if (handler == null) {
            throw new RuntimeException("Pas de handler enregistré pour : " + command.getClass());
        }
        return handler.handle(command);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C extends Command<R>, R> Flux<R> manyExecuteCommand(C command) {
        ManyCommandHandler<C, R> handler = (ManyCommandHandler<C, R>) manyHandlers.get(command.getClass());
        if (handler == null) {
            throw new RuntimeException("Pas de handler enregistré pour : " + command.getClass());
        }
        return handler.handle(command);
    }
}
