package com.ctoutweb.argenDePoche.infra.adapter.secondaryAdapter;

import com.ctoutweb.argentDePoche.application.query.dto.ChildAccountDto;
import com.ctoutweb.argentDePoche.application.query.dto.FamilyDto;
import com.ctoutweb.argentDePoche.application.repository.QueryRepository;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class QueryRepositoryAdapter implements QueryRepository {
    @Override
    public Optional<FamilyDto> loadFamily(FamilyAccountIdentity familyAccountId) {
        return Optional.empty();
    }

    @Override
    public Optional<ChildAccountDto> loadChildMoneyAccount(ChildMoneyAccountIdentity childMoneyAccountId) {
        return Optional.empty();
    }
}
