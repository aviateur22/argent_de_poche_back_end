package com.ctoutweb.argentDePoche.application.command.dto.command;

import com.ctoutweb.argentDePoche.application.command.UpdateCommand;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.childImage.ImageExtension;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;

public record StreamChildImageCommand(
        ParentIdentity parentIdentity,
        ChildMoneyAccountIdentity childMoneyAccountIdentity
) implements UpdateCommand<ImageExtension> {

  public static StreamChildImageCommand create(
          ParentIdentity parentIdentity,
          ChildMoneyAccountIdentity childMoneyAccountIdentity) {
    return new StreamChildImageCommand(parentIdentity, childMoneyAccountIdentity);

  }
  @Override
  public ChildMoneyAccountIdentity getChildAccountUpdated() {
    return childMoneyAccountIdentity;
  }

  @Override
  public ParentIdentity getParentUpdatedChildAccount() {
    return parentIdentity;
  }
}
