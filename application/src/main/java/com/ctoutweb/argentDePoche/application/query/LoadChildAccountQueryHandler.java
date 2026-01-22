package com.ctoutweb.argentDePoche.application.query;

import com.ctoutweb.argentDePoche.application.query.dto.ChildAccountDto;
import com.ctoutweb.argentDePoche.application.query.dto.query.LoadChildAccountQuery;
import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
import com.ctoutweb.argentDePoche.application.repository.QueryRepository;
import com.ctoutweb.argentDePoche.core.domain.exception.ChildMoneyAccountException;
import com.ctoutweb.argentDePoche.core.domain.exception.FamilyAccountException;
import com.ctoutweb.argentDePoche.application.policy.ChildAccessPolicy;
import com.ctoutweb.argentDePoche.application.policy.FamilyAccessPolicy;
import org.reactivestreams.Publisher;

public class LoadChildAccountQueryHandler implements QueryHandler<LoadChildAccountQuery, ChildAccountDto> {

    private final QueryRepository queryRepository;
    private final CommandRepository commandRepository;
    private final ChildAccessPolicy childAccessPolicy;
    private final FamilyAccessPolicy familyAccessPolicy;

    public LoadChildAccountQueryHandler(
            QueryRepository queryRepository, CommandRepository commandRepository,
            ChildAccessPolicy childAccessPolicy,
            FamilyAccessPolicy familyAccessPolicy) {
        this.queryRepository = queryRepository;
        this.commandRepository = commandRepository;
        this.childAccessPolicy = childAccessPolicy;
        this.familyAccessPolicy = familyAccessPolicy;
    }

    @Override
    public Publisher<ChildAccountDto> handle(LoadChildAccountQuery query) {
//        var childAccountRequested = query.childAccountRequested();
//        var parentRequestedChildAccount = query.parentRequestedChildAccount();
//
//        var family = commandRepository.loadFamilyAccountFromParent(parentRequestedChildAccount)
//                .orElseThrow(() -> new FamilyAccountException("Aucune famille existante"));
//
//        // Vérification authorisation
//        familyAccessPolicy.checkAccess(family.getParentIdentities(), parentRequestedChildAccount);
//
//        // Vérification que le parent peut accéder au compte de l'enfant
//        childAccessPolicy.checkAccess(childAccountRequested, family.getChildMoneyAccountIds());
//
//
//        return queryRepository.loadChildMoneyAccount(childAccountRequested)
//                .orElseThrow(() -> new ChildMoneyAccountException("Pas de compte disponible"));
        return null;
    }
}
