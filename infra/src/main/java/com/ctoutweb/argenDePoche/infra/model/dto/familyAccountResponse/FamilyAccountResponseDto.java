package com.ctoutweb.argenDePoche.infra.model.dto.familyAccountResponse;

import java.util.List;

public record FamilyAccountResponseDto(
        long familyAccountId,
        String familyName,
        List<ChildDto> childs) {
}
