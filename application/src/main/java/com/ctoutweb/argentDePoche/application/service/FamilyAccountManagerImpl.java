package com.ctoutweb.argentDePoche.application.service;

import com.ctoutweb.argentDePoche.application.configuration.bus.CommandBus;
import com.ctoutweb.argentDePoche.application.configuration.bus.EventBus;
import com.ctoutweb.argentDePoche.application.configuration.bus.QueryBus;
import com.ctoutweb.argentDePoche.application.api.FamilyAccountManager;
import com.ctoutweb.argentDePoche.application.policy.FamilyAccessPolicy;
import com.ctoutweb.argentDePoche.application.query.dto.FamilyDto;
import com.ctoutweb.argentDePoche.application.query.dto.query.LoadFamilyAccountQuery;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;
import org.reactivestreams.Publisher;

public class FamilyAccountManagerImpl implements FamilyAccountManager {
    private final QueryBus queryBus;
    private final FamilyAccessPolicy familyAccessPolicy;

    public FamilyAccountManagerImpl(EventBus eventBus, CommandBus commandBus, QueryBus queryBus, FamilyAccessPolicy accessAccountPolicy) {
        this.queryBus = queryBus;
        this.familyAccessPolicy = accessAccountPolicy;
    }


    @Override
    public Publisher<FamilyDto> loadFamilyAccount(ParentIdentity parentLoadingFamily, FamilyAccountIdentity familyToBeLoaded) {
        // Chargement de la famille
        var loadFamilyAccountQuery = LoadFamilyAccountQuery.create(familyToBeLoaded, parentLoadingFamily);
        return queryBus.executeQuery(loadFamilyAccountQuery);
    }
}
