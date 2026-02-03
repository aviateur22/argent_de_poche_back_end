package com.ctoutweb.argentDePoche.application.command.helper;

import com.ctoutweb.argentDePoche.application.configuration.annotation.CoreService;
import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccount;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.exception.ChildMoneyAccountException;
import reactor.core.publisher.Mono;

/**
 * Class permettant la réutilisation d'un chargement d'un aggregat ChildMoneyAccount
 * Il sera réutilisé dans les CommandHandler
 */
@CoreService
public final class LoaderChildAccount {

    private final CommandRepository commandRepository;

    public LoaderChildAccount(CommandRepository commandRepository) {
        this.commandRepository = commandRepository;
    }

    /**
     * Renvoie les données pour un compte d'un enfant
     *
     * @param childMoneyAccountId Le compte de l'enfant à charger
     *
     * @return ChildMoneyAccount
     */
    public Mono<ChildMoneyAccount> load(ChildMoneyAccountIdentity childMoneyAccountId) {

        // Chargement des données
        return commandRepository.loadActiveChildAccountAggregate(childMoneyAccountId)
                .switchIfEmpty(Mono.error(new ChildMoneyAccountException("Il n'y a pas de données associé à ce compte")));
    }
}
