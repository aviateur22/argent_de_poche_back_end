package com.ctoutweb.argentDePoche.application.service;

import com.ctoutweb.argentDePoche.application.api.ChildAccountManager;
import com.ctoutweb.argentDePoche.application.command.dto.command.*;
import com.ctoutweb.argentDePoche.application.configuration.annotation.CoreService;
import com.ctoutweb.argentDePoche.application.configuration.bus.CommandBus;
import com.ctoutweb.argentDePoche.application.configuration.bus.EventBus;
import com.ctoutweb.argentDePoche.application.configuration.bus.QueryBus;
import com.ctoutweb.argentDePoche.application.configuration.event.LogErrorEvent;
import com.ctoutweb.argentDePoche.application.port.AddMoneyMovement;
import com.ctoutweb.argentDePoche.application.port.ImageResource;
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
public class ChildAccountServiceImpl implements ChildAccountManager {

    private final CommandBus commandBus;
    private final QueryBus queryBus;
    private final RandomProvider randomProvider;
    private final EventBus eventBus;

    public ChildAccountServiceImpl(
            EventBus eventBus,
            CommandBus commandBus, QueryBus queryBus,
            RandomProvider randomProvider) {
        this.eventBus = eventBus;
        this.commandBus = commandBus;
        this.queryBus = queryBus;
        this.randomProvider = randomProvider;
    }

    @Override
    public Publisher<ChildAccountDto> loadChildAccount(
            ChildMoneyAccountIdentity childMoneyAccountId,
            ParentIdentity parentIdentity) {

        var loadChildAccountQuery = LoadChildAccountQuery.create(childMoneyAccountId, parentIdentity);
        return queryBus.executeQuery(loadChildAccountQuery);
    }

    @Override
    public Mono<ChildMoneyAccountIdentity> createChildMoneyAccount(
            ParentIdentity parentIdentity,
            String childName,
            String imagePath) {
        // Création d'un nouveau compte
        CreateChildAccountCommand createAccountCommand = CreateChildAccountCommand.create(parentIdentity, childName, imagePath);
        return commandBus.executeCommand(createAccountCommand);
    }

    @Override
    public Publisher<ChildMoneyAccountIdentity> updateChildImage(
            ChildMoneyAccountIdentity childMoneyAccountId,
            ImageResource updatedImage,
            ParentIdentity parentIdentity) {

        if(updatedImage == null || updatedImage.read().length == 0)
            throw new ChildImageException("La nouvelle image n'est pas valide");

        try {
            // Génération d'un nouveau nom aléatoire
            String newRandomImageName = randomProvider.generateUniqueRandomUuid();

            // Sauvegarde en base des nouvelle données
            UpdateChildImageCommand updateAccoundCommand = UpdateChildImageCommand
                    .create(childMoneyAccountId, parentIdentity, updatedImage, newRandomImageName);

            return commandBus.executeCommand(updateAccoundCommand);

        } catch (Exception exception) {
            eventBus.publish(new LogErrorEvent(exception.getMessage()));
            throw new ChildImageException("Echec de mise à jour de la nouvelle image", exception);
        }
    }

    @Override
    public Publisher<ChildMoneyAccountIdentity> addChildMoneyMovement(
            ChildMoneyAccountIdentity childMoneyAccountId,
            AddMoneyMovement addMoneyMovement,
            ParentIdentity parentUpdatedChildAccount) {

        // Sauvegarde en base des nouvelles données
        AddMoneyMovementCommand addMoneyMovementCommand = AddMoneyMovementCommand
                .create(parentUpdatedChildAccount, childMoneyAccountId, addMoneyMovement);
        return commandBus.executeCommand(addMoneyMovementCommand);
    }

    @Override
    public Publisher<ChildMoneyAccountIdentity> modulateInitialChildMoney(
            ChildMoneyAccountIdentity childMoneyAccountId,
            BigDecimal updatedInitialMoney,
            ParentIdentity parentUpdatedChildAccount) {
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
}
