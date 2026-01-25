package com.ctoutweb.argentDePoche.application.command.handler;

import com.ctoutweb.argentDePoche.application.command.MonoCommandHandler;
import com.ctoutweb.argentDePoche.application.command.dto.command.CreateFamilyAccountCommand;
import com.ctoutweb.argentDePoche.application.configuration.annotation.CoreService;
import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
import com.ctoutweb.argentDePoche.application.spi.NextIdentityProvider;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccount;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
import reactor.core.publisher.Mono;

@CoreService
public class CreateFamilyAccountCommandHandler implements MonoCommandHandler<CreateFamilyAccountCommand, FamilyAccountIdentity> {

    private final CommandRepository commandRepository;
    private final NextIdentityProvider nextIdentityProvider;

    public CreateFamilyAccountCommandHandler(CommandRepository commandRepository, NextIdentityProvider nextIdentityProvider) {
        this.commandRepository = commandRepository;
        this.nextIdentityProvider = nextIdentityProvider;
    }

    @Override
    public Mono<FamilyAccountIdentity> handle(CreateFamilyAccountCommand command) {
        // Generation des identitifiants pour la creation de la famile
        var nextFamilyIdentities = nextIdentityProvider.generateNextFamilyAccountIdentities();

        // Creation de l'aggregat famille
        var familyToCreate = FamilyAccount.create(
                nextFamilyIdentities.getNextFamilyAccountIdentity(),
                nextFamilyIdentities.getNexFamilyIdentity(),
                command.parentCreatingChildAccount(),
                command.familyName()
        );

        return commandRepository.createFamilyAccount(familyToCreate, command.parentCreatingChildAccount());
    }
}
