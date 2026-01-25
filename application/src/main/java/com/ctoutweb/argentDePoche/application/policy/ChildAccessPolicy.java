package com.ctoutweb.argentDePoche.application.policy;

import com.ctoutweb.argentDePoche.application.configuration.annotation.CoreService;
import com.ctoutweb.argentDePoche.application.exception.ChildAccountForbiddenException;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;

import java.util.List;

@CoreService
public class ChildAccessPolicy {

    public void checkAccess(
            ChildMoneyAccountIdentity childAccountUpdated,
            List<ChildMoneyAccountIdentity> childAccountInFamilyIds) {

        // Vérification le parent peut acceder au comte de l'enfant
        if (!childAccountInFamilyIds.contains(childAccountUpdated))
            throw new ChildAccountForbiddenException("Vous ne pouvez pas accéder à ce compte d'argent de poche");
    }
}
