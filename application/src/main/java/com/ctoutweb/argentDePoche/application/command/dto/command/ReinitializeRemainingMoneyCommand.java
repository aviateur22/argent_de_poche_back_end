package com.ctoutweb.argentDePoche.application.command.dto.command;

import com.ctoutweb.argentDePoche.application.command.UpdateCommand;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;

public record ReinitializeRemainingMoneyCommand(
        ChildMoneyAccountIdentity childAccountUpdated,
        ParentIdentity parentUpdatingChildAccount
) implements UpdateCommand<ChildMoneyAccountIdentity> {

  public static ReinitializeRemainingMoneyCommand create(ChildMoneyAccountIdentity childAccountUpdated, ParentIdentity parentUpdatingChildAccount) {
    return new ReinitializeRemainingMoneyCommand(childAccountUpdated, parentUpdatingChildAccount);
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
