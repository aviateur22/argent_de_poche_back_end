package com.ctoutweb.argentDePoche.application.command.dto.command;

import com.ctoutweb.argentDePoche.application.command.UpdateCommand;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;

public record DesactivateChildAccountCommand(
        ChildMoneyAccountIdentity childAccountUpdated,
        ParentIdentity parentUpdatingChildAccount
) implements UpdateCommand<ChildMoneyAccountIdentity> {

  public static DesactivateChildAccountCommand create(ChildMoneyAccountIdentity childAccountUpdated, ParentIdentity parentUpdatingChildAccount) {
    return new DesactivateChildAccountCommand(childAccountUpdated, parentUpdatingChildAccount);
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
