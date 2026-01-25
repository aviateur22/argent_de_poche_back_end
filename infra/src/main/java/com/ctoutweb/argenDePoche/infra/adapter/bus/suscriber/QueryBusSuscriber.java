package com.ctoutweb.argenDePoche.infra.adapter.bus.suscriber;

import com.ctoutweb.argentDePoche.application.configuration.bus.QueryBus;
import com.ctoutweb.argentDePoche.application.policy.ChildAccessPolicy;
import com.ctoutweb.argentDePoche.application.query.LoadChildAccountQueryHandler;
import com.ctoutweb.argentDePoche.application.query.LoadFamilyAccountQueryHandler;
import com.ctoutweb.argentDePoche.application.query.dto.query.LoadChildAccountQuery;
import com.ctoutweb.argentDePoche.application.query.dto.query.LoadFamilyAccountQuery;
import com.ctoutweb.argentDePoche.application.policy.FamilyAccessPolicy;
import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
import com.ctoutweb.argentDePoche.application.repository.QueryRepository;
import org.springframework.stereotype.Component;

@Component
public class QueryBusSuscriber {
    private final QueryBus queryBus;
    private final QueryRepository queryRepository;
    private final CommandRepository commandRepository;
    private final FamilyAccessPolicy familyAccessPolicy;
    private final ChildAccessPolicy childAccessPolicy;

    public QueryBusSuscriber(
            QueryBus queryBus,
            QueryRepository queryRepository,
            CommandRepository commandRepository,
            FamilyAccessPolicy familyAccessPolicy, ChildAccessPolicy childAccessPolicy) {
        this.queryBus = queryBus;
        this.queryRepository = queryRepository;
      this.commandRepository = commandRepository;
      this.familyAccessPolicy = familyAccessPolicy;
      this.childAccessPolicy = childAccessPolicy;

      register();
    }

    public void register() {
        queryBus.registerMonoHandler(LoadFamilyAccountQuery.class, new LoadFamilyAccountQueryHandler(queryRepository));
        queryBus.registerMonoHandler(LoadChildAccountQuery.class, new LoadChildAccountQueryHandler(queryRepository,commandRepository, childAccessPolicy, familyAccessPolicy));
    }
}
