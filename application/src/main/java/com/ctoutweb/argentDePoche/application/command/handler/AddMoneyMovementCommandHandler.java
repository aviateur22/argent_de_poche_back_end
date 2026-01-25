package com.ctoutweb.argentDePoche.application.command.handler;

import com.ctoutweb.argentDePoche.application.command.BaseCommandHandler;
import com.ctoutweb.argentDePoche.application.command.dto.command.AddMoneyMovementCommand;
import com.ctoutweb.argentDePoche.application.command.helper.LoaderChildAccount;
import com.ctoutweb.argentDePoche.application.configuration.annotation.CoreService;
import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.application.policy.ChildAccessPolicy;
import com.ctoutweb.argentDePoche.application.policy.FamilyAccessPolicy;
import reactor.core.publisher.Mono;

@CoreService
public class AddMoneyMovementCommandHandler extends BaseCommandHandler<AddMoneyMovementCommand, ChildMoneyAccountIdentity> {
    private final CommandRepository commandRepository;
    private final LoaderChildAccount loaderChildAccount;

    public AddMoneyMovementCommandHandler(
            LoaderChildAccount loaderChildAccount,
            CommandRepository commandRepository,
            FamilyAccessPolicy familyAccessPolicy,
            ChildAccessPolicy childAccessPolicy) {
        super(familyAccessPolicy, childAccessPolicy, commandRepository);
        this.commandRepository = commandRepository;
        this.loaderChildAccount = loaderChildAccount;
    }
    @Override
    protected Mono<ChildMoneyAccountIdentity> doHandle(AddMoneyMovementCommand command) {
//        // Chargement de l'aggregat du compte de l'enfant
//        var childAccountToUpdate = loaderChildAccount.load(command.childAccountUpdated());
//
//        // Creation du mouvement d'argent a ajouter dans le compte
//        var receiveMovement = command.moneyMovementToAdd();
//        MoneyMovement moneyMovementToAdd = MoneyMovement.create(
//                receiveMovement.getFluctuationPrice(),
//                receiveMovement.getMovementActionCode(),
//                receiveMovement.getMovementReasonCode(),
//                receiveMovement.getInitiatedByParent()
//        );
//
//        // Vérification du mouvement d'argent et ajout au compte de l'enfant
//        ChildMoneyAccount updatedChildMoneyAccount = childAccountToUpdate.addMoneyMovement(moneyMovementToAdd);
//
//        // Persistence de laggregat mis a jour
//        return commandRepository.updateChildMoneyAccount(updatedChildMoneyAccount);
        return null;
    }
}
