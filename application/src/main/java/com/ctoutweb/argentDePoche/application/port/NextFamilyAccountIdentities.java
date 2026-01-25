package com.ctoutweb.argentDePoche.application.port;

import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.family.FamilyIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;

/**
 * Contrat permettant de récupérer les identitifants lors de la creation
 * d'une famille
 */
public interface NextFamilyAccountIdentities {
    FamilyAccountIdentity getNextFamilyAccountIdentity();
    FamilyIdentity getNexFamilyIdentity();
}
