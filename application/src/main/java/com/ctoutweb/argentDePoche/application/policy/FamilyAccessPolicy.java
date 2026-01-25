package com.ctoutweb.argentDePoche.application.policy;

import com.ctoutweb.argentDePoche.application.configuration.annotation.CoreService;
import com.ctoutweb.argentDePoche.application.exception.FamilyAccountForbiddenException;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;

import java.util.List;

@CoreService
public class FamilyAccessPolicy {

    public void checkAccess(List<ParentIdentity> familyParents, ParentIdentity parentIdentity) {
        // Vérification le parent peut acceder à la famille qui a ete chargé
        if(familyParents
                .stream()
                .noneMatch(parentIdentityInFamilyAccount -> parentIdentityInFamilyAccount.equals(parentIdentity)))
            throw new FamilyAccountForbiddenException("Vous ne pouvez pas accéder à cette famille");
    }
}
