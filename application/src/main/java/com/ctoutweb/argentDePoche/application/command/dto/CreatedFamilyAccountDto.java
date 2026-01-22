package com.ctoutweb.argentDePoche.application.command.dto;

import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;

public record CreatedFamilyAccountDto(
        FamilyAccountIdentity createdFamilyAccountIdentiy,
        String familyName) {
}
