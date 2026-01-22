package com.ctoutweb.argentDePoche.application.repository;

import com.ctoutweb.argentDePoche.application.query.dto.ChildAccountDto;
import com.ctoutweb.argentDePoche.application.query.dto.FamilyDto;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;

import java.util.Optional;

public interface QueryRepository {
    Optional<FamilyDto> loadFamily(FamilyAccountIdentity familyAccountId);
    Optional<ChildAccountDto> loadChildMoneyAccount(ChildMoneyAccountIdentity childMoneyAccountId);
}
