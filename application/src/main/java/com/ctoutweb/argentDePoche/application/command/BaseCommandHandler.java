package com.ctoutweb.argentDePoche.application.command;

import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
import com.ctoutweb.argentDePoche.application.policy.ChildAccessPolicy;
import com.ctoutweb.argentDePoche.application.policy.FamilyAccessPolicy;
import com.ctoutweb.argentDePoche.core.domain.exception.FamilyAccountException;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

/**
 * Traitement de base des command Handler mettant à jour (update) le state
 * Il permets de d'effectuer les vérification d'accées au compte de famille et des enfant
 *
 * @param <C> Le type de la commande
 * @param <R> Le type de réponse à la commande
 */
public abstract class BaseCommandHandler<C extends UpdateCommand<R>, R> implements MonoCommandHandler<C , R> {
    private final FamilyAccessPolicy familyAccessPolicy;
    private final ChildAccessPolicy childAccessPolicy;
    private final CommandRepository commandRepository;

    protected BaseCommandHandler(FamilyAccessPolicy familyAccessPolicy, ChildAccessPolicy childAccessPolicy, CommandRepository commandRepository) {
        this.familyAccessPolicy = familyAccessPolicy;
        this.childAccessPolicy = childAccessPolicy;
        this.commandRepository = commandRepository;
    }

    @Override
    public final Mono<R> handle(C command) {
        var childAccountUpdated = command.getChildAccountUpdated();
        var parentUpdetedChildAccount = command.getParentUpdatedChildAccount();

        return commandRepository.loadFamilyAccountFromParent(parentUpdetedChildAccount)
                .switchIfEmpty(Mono.error(new FamilyAccountException("Aucune famille existante")))
                .flatMap(familyAccount -> {
                    // Vérification authorisation
                    familyAccessPolicy.checkAccess(familyAccount.getParentIdentities(), parentUpdetedChildAccount);

                    // Vérification que le parent peut modifier le compte de l'enfant
                    childAccessPolicy.checkAccess(childAccountUpdated, familyAccount.getChildMoneyAccountIds());

                    // Suite du processus géré par les enfants
                    return doHandle(command);
                });
    }

    protected abstract Mono<R> doHandle(C command);
}
