package com.ctoutweb.argentDePoche.application.command.dto.command;

import com.ctoutweb.argentDePoche.application.command.UpdateCommand;
import com.ctoutweb.argentDePoche.application.port.ImageResource;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;

public record UpdateChildImageCommand(
        ChildMoneyAccountIdentity childAccountUpdated,
        ParentIdentity parentUpdatingChildAccount,
        ImageResource newChildImage,
        String imageRandomName) implements UpdateCommand<ChildMoneyAccountIdentity> {
    public static UpdateChildImageCommand create(
            ChildMoneyAccountIdentity childAccountUpdated,
            ParentIdentity parentUpdatingAccount,
            ImageResource newChildImage,
            String imageRandomName) {
        return new UpdateChildImageCommand(childAccountUpdated, parentUpdatingAccount, newChildImage, imageRandomName);
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
