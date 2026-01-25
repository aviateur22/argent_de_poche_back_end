package com.ctoutweb.argentDePoche.application.command.dto;

import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;

public record CreatedFamilyAccountDto(
        ParentIdentity parentCreatingFamilyAccount,
        FamilyAccountIdentity createdFamilyAccountIdentiy,
        String familyName) {
}
