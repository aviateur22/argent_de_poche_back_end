package com.ctoutweb.argenDePoche.infra.model.dto.familyAccountResponse;

import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;

public record ChildDto(long childAccountId, String name, String imageRandomName) {
}
