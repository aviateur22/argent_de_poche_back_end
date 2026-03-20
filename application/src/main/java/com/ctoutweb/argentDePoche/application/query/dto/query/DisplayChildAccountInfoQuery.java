package com.ctoutweb.argentDePoche.application.query.dto.query;

import com.ctoutweb.argentDePoche.application.query.Query;
import com.ctoutweb.argentDePoche.application.query.dto.ChildAccountDto;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;

public record DisplayChildAccountInfoQuery(
        ChildMoneyAccountIdentity childAccountRequested) implements Query<ChildAccountDto> {

    /**
     * Instancie LoadChildMoneyAccountQuery
     *
     * @param childMoneyAccountId Identity du compte de l'enfant a charger
     *
     * @return LoadChildMoneyAccountQuery
     */
    public static DisplayChildAccountInfoQuery create(
            ChildMoneyAccountIdentity childMoneyAccountId) {
        return new DisplayChildAccountInfoQuery(childMoneyAccountId);
    }
}
