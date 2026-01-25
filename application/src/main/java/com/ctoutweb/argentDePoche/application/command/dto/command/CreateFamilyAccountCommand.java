package com.ctoutweb.argentDePoche.application.command.dto.command;

import com.ctoutweb.argentDePoche.application.command.Command;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;

public record CreateFamilyAccountCommand(
        ParentIdentity parentCreatingChildAccount,
        String familyName
) implements Command<FamilyAccountIdentity> {

    public static CreateFamilyAccountCommand create(ParentIdentity parentCreatingChildAccount, String familyName) {
        return new CreateFamilyAccountCommand(parentCreatingChildAccount, familyName);
    }
}
