package com.ctoutweb.argentDePoche.application.command.dto.command;

import com.ctoutweb.argentDePoche.application.command.UpdateCommand;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;

public record UpdateChildNameCommand(
        ParentIdentity parentUpdatingAccount,
        ChildMoneyAccountIdentity childAccountUpdated,
        String newChildName
) implements UpdateCommand<ChildMoneyAccountIdentity> {
    public static UpdateChildNameCommand create(
            ParentIdentity parentUpdatingAccount,
            ChildMoneyAccountIdentity childAccountUpdated,
            String newChildName) {
        return new UpdateChildNameCommand(parentUpdatingAccount, childAccountUpdated, newChildName);

    }

    @Override
    public ChildMoneyAccountIdentity getChildAccountUpdated() {
        return childAccountUpdated;
    }

    @Override
    public ParentIdentity getParentUpdatedChildAccount() {
        return parentUpdatingAccount;
    }
}
