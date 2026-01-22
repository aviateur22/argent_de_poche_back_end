package com.ctoutweb.argentDePoche.application.command.dto.command;

import com.ctoutweb.argentDePoche.application.command.Command;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;

public record CreateFamilyAccountCommand(
        String familyName
) implements Command<FamilyAccountIdentity> {

    public static CreateFamilyAccountCommand create( String familyName) {
        return new CreateFamilyAccountCommand(familyName);
    }
}
