package com.ctoutweb.argentDePoche.application.command.handler;

import com.ctoutweb.argentDePoche.application.command.BaseCommandHandler;
import com.ctoutweb.argentDePoche.application.command.dto.command.AddMoneyMovementCommand;
import com.ctoutweb.argentDePoche.application.command.helper.LoaderChildAccount;
import com.ctoutweb.argentDePoche.application.configuration.annotation.CoreService;
import com.ctoutweb.argentDePoche.application.exception.BalanceReasonException;
import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.application.policy.ChildAccessPolicy;
import com.ctoutweb.argentDePoche.application.policy.FamilyAccessPolicy;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account.MoneyMovement;
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

        // Creation du mouvement d'argent a ajouter dans le compte
        var movementReasonCode = command.mouvementReasonCode();
        var movementActionCode = command.mouvementActionCode();
        var childAccountToBeUpdated = command.childAccountUpdated();
        var parentAddingMovement = command.parentAddingMoneyMovement();

        var movementReasonInformation =  commandRepository.loadMoneyMovementReasonByCode(movementReasonCode, childAccountToBeUpdated)
                .switchIfEmpty(Mono.error(new BalanceReasonException("Le code de la raison du mouvement d'argent n'est pas valable")));

        var childAccountBeforeUpdate = loaderChildAccount.load(childAccountToBeUpdated);

        return Mono.zip(movementReasonInformation, childAccountBeforeUpdate)
        .flatMap(tupleData -> {
           var  movementReasonByChildAccount = tupleData.getT1();
           var childAccount = tupleData.getT2();

            MoneyMovement moneyMovementToAdd = MoneyMovement.create(
                    movementReasonByChildAccount.getFluctuationPrice(),
                    movementActionCode,
                    movementReasonCode,
                    parentAddingMovement
            );

            var updatedChildAccount = childAccount.addMoneyMovement(moneyMovementToAdd);

            return commandRepository.updateActiveChildMoneyAccount(updatedChildAccount)
            .then(commandRepository.addMoneyMovement(moneyMovementToAdd, childAccountToBeUpdated));
        });
    }
}
