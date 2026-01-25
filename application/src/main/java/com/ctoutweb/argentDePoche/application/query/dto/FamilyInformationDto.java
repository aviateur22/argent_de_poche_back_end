package com.ctoutweb.argentDePoche.application.query.dto;

import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;

public record FamilyInformationDto(FamilyAccountIdentity familyAccountIdentity, String familyName)  {
}
