package com.ctoutweb.argentDePoche.application.command.dto.command;

import com.ctoutweb.argentDePoche.application.command.UpdateCommand;
import com.ctoutweb.argentDePoche.application.command.dto.UpdatedChildImage;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;

public record UpdateChildImageCommand(
        ChildMoneyAccountIdentity childAccountUpdated,
        ParentIdentity parentUpdatingChildAccount,
        String imageRandomName) implements UpdateCommand<UpdatedChildImage> {
    public static UpdateChildImageCommand create(
            ChildMoneyAccountIdentity childAccountUpdated,
            ParentIdentity parentUpdatingAccount,
            String imageRandomName) {
        return new UpdateChildImageCommand(childAccountUpdated, parentUpdatingAccount, imageRandomName);
    }

    @Override
    public ChildMoneyAccountIdentity getChildAccountUpdated() {
        return childAccountUpdated;
    }

    @Override
    public ParentIdentity getParentUpdatedChildAccount() {
        return parentUpdatingChildAccount;
    }
}
