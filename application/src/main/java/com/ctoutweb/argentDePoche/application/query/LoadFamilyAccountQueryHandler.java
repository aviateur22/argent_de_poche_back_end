package com.ctoutweb.argentDePoche.application.query;

import com.ctoutweb.argentDePoche.application.query.dto.FamilyDto;
import com.ctoutweb.argentDePoche.application.query.dto.query.LoadFamilyAccountQuery;
import com.ctoutweb.argentDePoche.application.policy.FamilyAccessPolicy;
import com.ctoutweb.argentDePoche.application.repository.QueryRepository;
import com.ctoutweb.argentDePoche.core.domain.exception.FamilyAccountException;
import org.reactivestreams.Publisher;

public class LoadFamilyAccountQueryHandler implements QueryHandler<LoadFamilyAccountQuery, FamilyDto> {

    private final QueryRepository queryRepository;
    private final FamilyAccessPolicy familyAccessPolicy;

    public LoadFamilyAccountQueryHandler(
            QueryRepository queryRepository,
            FamilyAccessPolicy familyAccessPolicy) {
        this.queryRepository = queryRepository;
        this.familyAccessPolicy = familyAccessPolicy;
    }

    @Override
    public Publisher<FamilyDto> handle(LoadFamilyAccountQuery loadFamilyAccountQuery) {

//        var familyToBeLoaded = loadFamilyAccountQuery.familyAccountId();
//        var parentLoadingFamily = loadFamilyAccountQuery.parentIdentity();
//
//        var family = queryRepository.loadFamily(familyToBeLoaded)
//                .orElseThrow(() -> new FamilyAccountException("Aucune famille existante"));
//
//        // Vérification des droit d'accés à la famille
//        familyAccessPolicy.checkAccess(family.familyParentIdentities(), parentLoadingFamily);
//
//        return family;
        return null;
    }
}
