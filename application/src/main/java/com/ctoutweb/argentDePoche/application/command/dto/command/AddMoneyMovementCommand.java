package com.ctoutweb.argentDePoche.application.command.dto.command;

import com.ctoutweb.argentDePoche.application.command.UpdateCommand;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;

public record AddMoneyMovementCommand(
        ParentIdentity parentAddingMoneyMovement,
        ChildMoneyAccountIdentity childAccountUpdated,
        String mouvementReasonCode,
        String mouvementActionCode) implements UpdateCommand<ChildMoneyAccountIdentity> {
    public static AddMoneyMovementCommand create(
            ParentIdentity parentAddingMoneyMovement,
            ChildMoneyAccountIdentity childImpacted,
            String mouvementReasonCode,
            String mouvementActionCode) {
        return new AddMoneyMovementCommand(parentAddingMoneyMovement, childImpacted, mouvementReasonCode, mouvementActionCode);
    }

    @Override
    public ChildMoneyAccountIdentity getChildAccountUpdated() {
        return childAccountUpdated;
    }

    @Override
    public ParentIdentity getParentUpdatedChildAccount() {
        return parentAddingMoneyMovement;
    }
}
