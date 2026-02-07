package com.ctoutweb.argentDePoche.application.command.handler;

import com.ctoutweb.argentDePoche.application.command.MonoCommandHandler;
import com.ctoutweb.argentDePoche.application.command.dto.command.CreateChildAccountCommand;
import com.ctoutweb.argentDePoche.application.configuration.annotation.CoreService;
import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
import com.ctoutweb.argentDePoche.application.spi.NextIdentityProvider;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccount;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.application.policy.FamilyAccessPolicy;
import com.ctoutweb.argentDePoche.core.domain.exception.FamilyAccountException;
import reactor.core.publisher.Mono;

/**
 * Creation d'un nouveau compte pour enfant
 */
@CoreService
public class CreateChildAccountCommandHandler implements MonoCommandHandler<CreateChildAccountCommand, ChildMoneyAccountIdentity> {

    private final CommandRepository commandRepository;
    private final FamilyAccessPolicy familyAccessPolicy;
    private final NextIdentityProvider nextIdentityProvider;

    public CreateChildAccountCommandHandler(CommandRepository commandRepository, FamilyAccessPolicy familyAccessPolicy, NextIdentityProvider nextIdentityProvider) {
        this.commandRepository = commandRepository;
        this.familyAccessPolicy = familyAccessPolicy;
        this.nextIdentityProvider = nextIdentityProvider;
    }

    @Override
    public Mono<ChildMoneyAccountIdentity> handle(CreateChildAccountCommand createChildAccountCommand) {
        var parentCreatingChildAccount = createChildAccountCommand.parentCreatingChildAccount();

        return commandRepository.loadFamilyAccountFromParent(parentCreatingChildAccount)
                .switchIfEmpty( Mono.error(new FamilyAccountException("Aucune famille existante")))
                .flatMap(familyAccount -> {
                    familyAccessPolicy.checkAccess(familyAccount.getParentIdentities(), parentCreatingChildAccount);

                    // Generation des identities necessaire au compte d'argent de poche
                    var nextChildAccountIdentities = this.nextIdentityProvider.generateNextChildAccountIdentities();

                    // Creation de l'aggregat
                    var childMoneyAccount = ChildMoneyAccount.createDefaultChildAccount(
                            nextChildAccountIdentities.getNextChildMoneyAccountId(),
                            nextChildAccountIdentities.getNextChildIdentity(),
                            createChildAccountCommand.childName(),
                            createChildAccountCommand.defaultImageName(),
                            createChildAccountCommand.imageExtension()
                    );

                    // Peristance du compte
                    return commandRepository.createChildMoneyAccount(childMoneyAccount, familyAccount);
                });
    }
}
