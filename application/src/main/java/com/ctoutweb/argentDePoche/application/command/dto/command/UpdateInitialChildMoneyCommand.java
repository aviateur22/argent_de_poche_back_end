package com.ctoutweb.argentDePoche.application.command.dto.command;

import com.ctoutweb.argentDePoche.application.command.UpdateCommand;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;

import java.math.BigDecimal;

public record UpdateInitialChildMoneyCommand(
        ChildMoneyAccountIdentity childAccountUpdated,
        ParentIdentity parentUpdatedChildAccount,
        BigDecimal moneyAtPeriodStart) implements UpdateCommand<ChildMoneyAccountIdentity> {
    public static UpdateInitialChildMoneyCommand create(
            ParentIdentity parentUpdatedChildAccount,
            ChildMoneyAccountIdentity childAccountUpdated,
            BigDecimal moneyAtPeriodStart
            ) {
        return new UpdateInitialChildMoneyCommand(childAccountUpdated, parentUpdatedChildAccount, moneyAtPeriodStart);
    }

    @Override
    public ChildMoneyAccountIdentity getChildAccountUpdated() {
        return childAccountUpdated;
    }

    @Override
    public ParentIdentity getParentUpdatedChildAccount() {
        return parentUpdatedChildAccount;
    }
}
