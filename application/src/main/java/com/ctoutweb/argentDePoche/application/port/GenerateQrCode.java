package com.ctoutweb.argentDePoche.application.port;

import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;

public interface GenerateQrCode {
  ChildMoneyAccountIdentity getChildMoneyAccountId();
  ParentIdentity getParentIdentity();
  String getUrlToDisplayInQrCode();
}
