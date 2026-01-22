package com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate;

import com.ctoutweb.argentDePoche.core.domain.base.identity.Identity;

public class ChildMoneyAccountIdentity extends Identity<Long> {

    public ChildMoneyAccountIdentity(Long id) {
        super(id);
    }
}
