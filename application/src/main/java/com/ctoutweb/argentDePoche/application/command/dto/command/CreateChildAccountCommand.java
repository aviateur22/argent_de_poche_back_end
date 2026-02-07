package com.ctoutweb.argentDePoche.application.command.dto.command;

import com.ctoutweb.argentDePoche.application.command.Command;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.childImage.ImageExtension;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;

public record CreateChildAccountCommand(
        ParentIdentity parentCreatingChildAccount,
        String childName,
        String defaultImageName,
        ImageExtension imageExtension) implements Command<ChildMoneyAccountIdentity> {
    public static CreateChildAccountCommand create(
            ParentIdentity parentCreatingChildAccount,
            String childName,
            String defaultImageName,
            ImageExtension imageExtension) {
        return new CreateChildAccountCommand(parentCreatingChildAccount, childName, defaultImageName, imageExtension);
    }
}
