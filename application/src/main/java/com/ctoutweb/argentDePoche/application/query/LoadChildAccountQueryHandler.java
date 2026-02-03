package com.ctoutweb.argentDePoche.application.query;

import com.ctoutweb.argentDePoche.application.exception.FamilyAccountForbiddenException;
import com.ctoutweb.argentDePoche.application.query.dto.ChildAccountDto;
import com.ctoutweb.argentDePoche.application.query.dto.query.LoadChildAccountQuery;
import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
import com.ctoutweb.argentDePoche.application.repository.QueryRepository;
import com.ctoutweb.argentDePoche.application.policy.ChildAccessPolicy;
import com.ctoutweb.argentDePoche.application.policy.FamilyAccessPolicy;
import reactor.core.publisher.Mono;

public class LoadChildAccountQueryHandler implements MonoQueryHandler<LoadChildAccountQuery, ChildAccountDto> {

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
    public Mono<ChildAccountDto> handle(LoadChildAccountQuery query) {

       var childAccountRequested = query.childAccountRequested();
       var parentRequestedChildAccount = query.parentRequestedChildAccount();
       return commandRepository.loadFamilyAccountFromParent(parentRequestedChildAccount)
               .switchIfEmpty(Mono.error(new FamilyAccountForbiddenException("Aucun compte de famille n'est associé à votre compte")))
               .flatMap(familyAccount -> {
                    // Vérification authorisation
                    familyAccessPolicy.checkAccess(familyAccount.getParentIdentities(), parentRequestedChildAccount);

                   // Vérification que le parent peut accéder au compte de l'enfant
                   childAccessPolicy.checkAccess(childAccountRequested, familyAccount.getChildMoneyAccountIds());
                   return queryRepository.loadActiveChildMoneyAccount(childAccountRequested);
               });
    }
}
