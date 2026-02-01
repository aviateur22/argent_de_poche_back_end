package com.ctoutweb.argentDePoche.application.api.impl;

import com.ctoutweb.argentDePoche.application.api.ChildAccountUseCase;
import com.ctoutweb.argentDePoche.application.command.dto.UpdatedChildImage;
import com.ctoutweb.argentDePoche.application.command.dto.command.*;
import com.ctoutweb.argentDePoche.application.configuration.annotation.CoreService;
import com.ctoutweb.argentDePoche.application.configuration.bus.CommandBus;
import com.ctoutweb.argentDePoche.application.configuration.bus.EventBus;
import com.ctoutweb.argentDePoche.application.configuration.bus.QueryBus;
import com.ctoutweb.argentDePoche.application.configuration.event.LogErrorEvent;
import com.ctoutweb.argentDePoche.application.port.AddMoneyMovementReason;
import com.ctoutweb.argentDePoche.application.query.dto.ChildAccountDto;
import com.ctoutweb.argentDePoche.application.query.dto.query.LoadChildAccountQuery;
import com.ctoutweb.argentDePoche.application.spi.RandomProvider;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.exception.ChildImageException;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@CoreService
public class ChildAccountUseCaseImpl implements ChildAccountUseCase {

    private final CommandBus commandBus;
    private final QueryBus queryBus;
    private final RandomProvider randomProvider;
    private final EventBus eventBus;

    public ChildAccountUseCaseImpl(
            EventBus eventBus,
            CommandBus commandBus, QueryBus queryBus,
            RandomProvider randomProvider) {
        this.eventBus = eventBus;
        this.commandBus = commandBus;
        this.queryBus = queryBus;
        this.randomProvider = randomProvider;
    }

    @Override
    public Mono<ChildAccountDto> loadChildAccount(
            ChildMoneyAccountIdentity childMoneyAccountId,
            ParentIdentity parentIdentity) {

        var loadChildAccountQuery = LoadChildAccountQuery.create(childMoneyAccountId, parentIdentity);
        return queryBus.executeQuery(loadChildAccountQuery);
    }

    @Override
    public Mono<ChildMoneyAccountIdentity> createChildMoneyAccount(
            ParentIdentity parentIdentity,
            String childName) {
        // Création d'un nouveau compte
        CreateChildAccountCommand createAccountCommand = CreateChildAccountCommand.create(parentIdentity, childName);
        return commandBus.executeCommand(createAccountCommand);
    }

    @Override
    public Mono<UpdatedChildImage> updateChildImage(
            ChildMoneyAccountIdentity childMoneyAccountId,
            ParentIdentity parentIdentity) {
        try {
            // Génération d'un nouveau nom aléatoire
            String newRandomImageName = randomProvider.generateUniqueRandomUuid();

            // Sauvegarde en base des nouvelle données
            UpdateChildImageCommand updateAccoundCommand = UpdateChildImageCommand
                    .create(childMoneyAccountId, parentIdentity, newRandomImageName);

            return commandBus.executeCommand(updateAccoundCommand);

        } catch (Exception exception) {
            eventBus.publish(new LogErrorEvent(exception.getMessage()));
            throw new ChildImageException("Echec de mise à jour de la nouvelle image", exception);
        }
    }

    @Override
    public Mono<ChildMoneyAccountIdentity> addChildMoneyMovement(
            ChildMoneyAccountIdentity childMoneyAccountId,
            ParentIdentity parentUpdatedChildAccount,
            String movementReasonCode,
            String movementActionCode) {

        // Sauvegarde en base des nouvelles données
        AddMoneyMovementCommand addMoneyMovementCommand = AddMoneyMovementCommand
                .create(parentUpdatedChildAccount, childMoneyAccountId, movementReasonCode, movementActionCode);
        return commandBus.executeCommand(addMoneyMovementCommand);
    }

    @Override
    public Mono<ChildMoneyAccountIdentity> modulateInitialChildMoney(
            ChildMoneyAccountIdentity childMoneyAccountId,
            ParentIdentity parentUpdatedChildAccount,
            BigDecimal updatedInitialMoney) {
        // Persistance des nouvelles données
        UpdateInitialChildMoneyCommand updateInitialChildMoneyCommand = UpdateInitialChildMoneyCommand
                .create(parentUpdatedChildAccount, childMoneyAccountId, updatedInitialMoney);

        // Mise a jour des données
        return commandBus.executeCommand(updateInitialChildMoneyCommand);
    }

    @Override
    public Publisher<ChildMoneyAccountIdentity> updateChildName(
            ChildMoneyAccountIdentity childMoneyAccountId,
            String updatedChildName,
            ParentIdentity parentUpdatedChildAccount) {
        // Persistance des nouvelles données
        UpdateChildNameCommand updateChildNameCommand = UpdateChildNameCommand
                .create(parentUpdatedChildAccount, childMoneyAccountId, updatedChildName);

        // Mise a jour des données
        return commandBus.executeCommand(updateChildNameCommand);
    }

    @Override
    public Mono<Boolean> initializeNextCalendarPeriod() {
        InitializeNextPeriodCommand initializeNextPeriodCommand = new InitializeNextPeriodCommand();
        return commandBus.executeCommand(initializeNextPeriodCommand);
    }
}
