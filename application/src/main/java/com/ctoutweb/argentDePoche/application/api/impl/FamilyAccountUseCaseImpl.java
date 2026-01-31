package com.ctoutweb.argentDePoche.application.api.impl;

import com.ctoutweb.argentDePoche.application.command.dto.command.CreateFamilyAccountCommand;
import com.ctoutweb.argentDePoche.application.configuration.annotation.CoreService;
import com.ctoutweb.argentDePoche.application.configuration.bus.CommandBus;
import com.ctoutweb.argentDePoche.application.configuration.bus.QueryBus;
import com.ctoutweb.argentDePoche.application.api.FamilyAccountUseCase;
import com.ctoutweb.argentDePoche.application.policy.FamilyAccessPolicy;
import com.ctoutweb.argentDePoche.application.query.dto.FamilyDto;
import com.ctoutweb.argentDePoche.application.query.dto.query.LoadFamilyAccountQuery;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;
import reactor.core.publisher.Mono;

@CoreService
public class FamilyAccountUseCaseImpl implements FamilyAccountUseCase {
    private final QueryBus queryBus;
    private final CommandBus commandBus;

    private final FamilyAccessPolicy familyAccessPolicy;

    public FamilyAccountUseCaseImpl(
            QueryBus queryBus,
            CommandBus commandBus,
            FamilyAccessPolicy accessAccountPolicy) {
        this.queryBus = queryBus;
      this.commandBus = commandBus;
      this.familyAccessPolicy = accessAccountPolicy;
    }


    @Override
    public Mono<FamilyDto> loadFamilyAccount(ParentIdentity parentLoadingFamily) {
        // Chargement de la famille
        var loadFamilyAccountQuery = LoadFamilyAccountQuery.create(parentLoadingFamily);
        return queryBus.executeQuery(loadFamilyAccountQuery);
    }

    @Override
    public Mono<FamilyAccountIdentity> createFamilyAccount(ParentIdentity parentIdentity, String familyName) {
        var command = CreateFamilyAccountCommand.create(parentIdentity, familyName);
        return commandBus.executeCommand(command);
    }


}
