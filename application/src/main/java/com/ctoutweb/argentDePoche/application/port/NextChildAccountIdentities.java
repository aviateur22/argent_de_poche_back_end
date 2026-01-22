package com.ctoutweb.argentDePoche.application.port;

import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.child.ChildIdentity;
import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.childImage.ChildImageIdentity;

/**
 * Contrat permattant de recvoir les prochaines Identité pour la creation d'un nouveau compte
 */
public interface NextChildAccountIdentities {
    ChildIdentity getNextChildIdentity();
    ChildMoneyAccountIdentity getNextChildMoneyAccountId();
}
