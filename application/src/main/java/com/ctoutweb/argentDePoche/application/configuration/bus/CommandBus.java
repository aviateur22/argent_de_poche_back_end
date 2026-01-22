package com.ctoutweb.argentDePoche.application.configuration.bus;


import com.ctoutweb.argentDePoche.application.command.Command;
import com.ctoutweb.argentDePoche.application.command.ManyCommandHandler;
import com.ctoutweb.argentDePoche.application.command.MonoCommandHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Contrat permettant d'enregistrer les Commandes handler
 */
public interface CommandBus {

    /**
     * Enregistrement d'un CommandHanler pour une commande specific renvoyant un objet unique
     *
     * @param commandType La class du CommandHandler
     * @param handler Le commandHandler
     * @param <C> Le type de la commande
     * @param <R> Le type renvoyé par la command
     */
    <C extends Command<R>, R> void registerMonoHandler(Class<C> commandType, MonoCommandHandler<C, R> handler);

    /**
     * Enregistrement d'un CommandHanler pour une commande renvoyant de multiples objets
     *
     * @param commandType La class du CommandHandler
     * @param handler Le commandHandler
     * @param <C> Le type de la commande
     * @param <R> Le type renvoyé par la command
     */
    <C extends Command<R>, R> void registerManyHandler(Class<C> commandType, ManyCommandHandler<C, R> handler);

    /**
     * Execution d'une commande renvoyer un objet unique
     *
     * @param command Le type de command a executer
     *
     * @param <C> Le type de la commande
     * @param <R> Le type renvoyer par la commande
     *
     * @return Le type de resultat renvoyer par la commande
     */
    <C extends Command<R>, R> Mono<R> executeCommand(C command);
    <C extends Command<R>, R> Flux<R> manyExecuteCommand(C command);
}
