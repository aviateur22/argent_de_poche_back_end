package com.ctoutweb.argentDePoche.application.command.dto;

import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;

public record CreatedChildAccountDto(
        ChildMoneyAccountIdentity createdChildAccountIdentity,
        String imageRandomName,
        String imageRegisterPath,
        String childName) {
}
