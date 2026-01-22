package com.ctoutweb.argenDePoche.infra.adapter.bus.suscribe;

import com.ctoutweb.argentDePoche.application.configuration.bus.QueryBus;
import com.ctoutweb.argentDePoche.application.query.LoadFamilyAccountQueryHandler;
import com.ctoutweb.argentDePoche.application.query.dto.query.LoadFamilyAccountQuery;
import com.ctoutweb.argentDePoche.application.policy.FamilyAccessPolicy;
import com.ctoutweb.argentDePoche.application.repository.QueryRepository;
import org.springframework.stereotype.Component;

@Component
public class QueryBusSuscriber {
    private final QueryBus queryBus;
    private final QueryRepository queryRepository;
    private final FamilyAccessPolicy familyAccessPolicy;

    public QueryBusSuscriber(
            QueryBus queryBus,
            QueryRepository queryRepository,
            FamilyAccessPolicy familyAccessPolicy) {
        this.queryBus = queryBus;
        this.queryRepository = queryRepository;
        this.familyAccessPolicy = familyAccessPolicy;
    }

    public void register() {
        queryBus.registerHandler(LoadFamilyAccountQuery.class, new LoadFamilyAccountQueryHandler(queryRepository, familyAccessPolicy));
    }
}
