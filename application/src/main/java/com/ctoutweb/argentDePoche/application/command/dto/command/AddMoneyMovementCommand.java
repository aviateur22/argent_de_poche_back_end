package com.ctoutweb.argentDePoche.application.command.dto.command;

import com.ctoutweb.argentDePoche.application.command.UpdateCommand;
import com.ctoutweb.argentDePoche.application.port.AddMoneyMovement;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;

public record AddMoneyMovementCommand(
        ParentIdentity parentAddingMoneyMovement,
        ChildMoneyAccountIdentity childAccountUpdated,
        AddMoneyMovement moneyMovementToAdd) implements UpdateCommand<ChildMoneyAccountIdentity> {
    public static AddMoneyMovementCommand create(
            ParentIdentity parentAddingMoneyMovement,
            ChildMoneyAccountIdentity childImpacted,
            AddMoneyMovement moneyMovementToAdd) {
        return new AddMoneyMovementCommand(parentAddingMoneyMovement, childImpacted, moneyMovementToAdd);
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
