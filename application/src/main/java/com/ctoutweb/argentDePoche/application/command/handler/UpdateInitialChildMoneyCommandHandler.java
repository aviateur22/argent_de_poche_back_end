package com.ctoutweb.argentDePoche.application.command.handler;

import com.ctoutweb.argentDePoche.application.command.BaseCommandHandler;
import com.ctoutweb.argentDePoche.application.command.dto.command.UpdateInitialChildMoneyCommand;
import com.ctoutweb.argentDePoche.application.command.helper.LoaderChildAccount;
import com.ctoutweb.argentDePoche.application.configuration.annotation.CoreService;
import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.application.policy.ChildAccessPolicy;
import com.ctoutweb.argentDePoche.application.policy.FamilyAccessPolicy;
import reactor.core.publisher.Mono;

@CoreService
public class UpdateInitialChildMoneyCommandHandler extends BaseCommandHandler<UpdateInitialChildMoneyCommand, ChildMoneyAccountIdentity> {

    private final CommandRepository commandRepository;
    private final LoaderChildAccount loaderChildAccount;

    public UpdateInitialChildMoneyCommandHandler(
            CommandRepository childAccountRepository,
            LoaderChildAccount loaderChildAccount,
            ChildAccessPolicy childAccessPolicy,
            FamilyAccessPolicy familyAccessPolicy) {
        super(familyAccessPolicy, childAccessPolicy, childAccountRepository);
        this.commandRepository = childAccountRepository;
        this.loaderChildAccount = loaderChildAccount;
    }

    @Override

    protected Mono<ChildMoneyAccountIdentity> doHandle(UpdateInitialChildMoneyCommand command) {
        // chargement des données du compte de l'enfant
        return loaderChildAccount.load(command.childAccountUpdated())
            .flatMap(childAccountMoney -> {
                var childAccountUpdated = childAccountMoney.updateInitialMoneyAtPeriodStart(command.moneyAtPeriodStart());
                // Sauvegarde de la mise a jour du compte
                return commandRepository.updateActiveChildMoneyAccount(childAccountUpdated);
            });
    }
}
