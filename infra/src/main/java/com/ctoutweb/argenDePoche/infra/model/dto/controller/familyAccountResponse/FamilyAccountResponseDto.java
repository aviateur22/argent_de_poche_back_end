package com.ctoutweb.argenDePoche.infra.model.dto.controller.familyAccountResponse;

import java.util.List;

public record FamilyAccountResponseDto(
        long familyAccountId,
        String familyName,
        List<ChildDto> childs) {
}
