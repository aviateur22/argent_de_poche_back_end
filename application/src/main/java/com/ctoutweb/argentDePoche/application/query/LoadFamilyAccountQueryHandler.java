package com.ctoutweb.argentDePoche.application.query;

import com.ctoutweb.argentDePoche.application.exception.FamilyAccountForbiddenException;
import com.ctoutweb.argentDePoche.application.exception.FamilyConflictException;
import com.ctoutweb.argentDePoche.application.query.dto.FamilyDto;
import com.ctoutweb.argentDePoche.application.query.dto.query.LoadFamilyAccountQuery;
import com.ctoutweb.argentDePoche.application.repository.QueryRepository;
import reactor.core.publisher.Mono;

public class LoadFamilyAccountQueryHandler implements MonoQueryHandler<LoadFamilyAccountQuery, FamilyDto> {

    private final QueryRepository queryRepository;

    public LoadFamilyAccountQueryHandler(QueryRepository queryRepository) {
        this.queryRepository = queryRepository;

    }

    @Override
    public Mono<FamilyDto> handle(LoadFamilyAccountQuery loadFamilyAccountQuery) {

        var parentLoadingFamily = loadFamilyAccountQuery.parentIdentity();

        return queryRepository.loadFamilyAccountsByParent(parentLoadingFamily)
                .collectList()
                .flatMap(parentFamilies -> {
                    if(parentFamilies.size() > 1)
                        return Mono.error(new FamilyConflictException("Actuellement un parent ne peut être associé que à un compte familiale"));

                    if(parentFamilies.isEmpty())
                        return Mono.error(new FamilyAccountForbiddenException("Aucun compte de famille n'est associé à votre compte"));

                    // Récupéra de la famille
                  var family = parentFamilies.get(0);
                    return queryRepository.loadChildAccountsByFamily(family.familyAccountIdentity())
                            .collectList()
                            .map(childAccounts -> new FamilyDto(
                                    family.familyAccountIdentity(),
                                    family.familyName(),
                                    childAccounts
                            ));
                });
    }
}
