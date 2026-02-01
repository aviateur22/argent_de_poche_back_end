package com.ctoutweb.argentDePoche.application.command.handler;

import com.ctoutweb.argentDePoche.application.command.BaseCommandHandler;
import com.ctoutweb.argentDePoche.application.command.dto.command.UpdateChildNameCommand;
import com.ctoutweb.argentDePoche.application.command.helper.LoaderChildAccount;
import com.ctoutweb.argentDePoche.application.configuration.annotation.CoreService;
import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.application.policy.ChildAccessPolicy;
import com.ctoutweb.argentDePoche.application.policy.FamilyAccessPolicy;
import reactor.core.publisher.Mono;

@CoreService
public class UpdateChildNameCommandHandler extends BaseCommandHandler<UpdateChildNameCommand, ChildMoneyAccountIdentity> {
    private final CommandRepository commandRepository;
    private final LoaderChildAccount loaderChildAccount;

    protected UpdateChildNameCommandHandler(FamilyAccessPolicy familyAccessPolicy, ChildAccessPolicy childAccessPolicy, CommandRepository commandRepository, LoaderChildAccount loaderChildAccount) {
        super(familyAccessPolicy, childAccessPolicy, commandRepository);
        this.loaderChildAccount = loaderChildAccount;
        this.commandRepository = commandRepository;
    }

    @Override
    protected Mono<ChildMoneyAccountIdentity> doHandle(UpdateChildNameCommand command) {
        // chargement des données du compte de l'enfant
        return loaderChildAccount.load(command.childAccountUpdated())
                .flatMap(childMoneyAccount -> {
                    var childAccountUpdated = childMoneyAccount.updateChildName(command.newChildName());
                    // Sauvegarde de la mise a jour du compte
                    return commandRepository.updateActiveChildMoneyAccount(childAccountUpdated);
                });


    }
}
