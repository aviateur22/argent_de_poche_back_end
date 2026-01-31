package com.ctoutweb.argentDePoche.application.command.dto;

import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;

public record UpdatedChildImage(
        ChildMoneyAccountIdentity childMoneyAccountIdentity,
        String newRandomImageName,
        String oldImageName) {
}
