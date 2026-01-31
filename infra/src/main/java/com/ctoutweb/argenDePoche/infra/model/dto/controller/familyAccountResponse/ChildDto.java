package com.ctoutweb.argenDePoche.infra.model.dto.controller.familyAccountResponse;

import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;

public record ChildDto(long childAccountId, String name, String imageRandomName) {
}
