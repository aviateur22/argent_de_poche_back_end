package com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate;

import com.ctoutweb.argentDePoche.core.domain.base.identity.Identity;

public class FamilyAccountIdentity extends Identity<Long> {
    public FamilyAccountIdentity(Long id) {
        super(id);
    }
}
