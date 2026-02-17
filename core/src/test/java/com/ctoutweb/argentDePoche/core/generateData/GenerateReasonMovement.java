package com.ctoutweb.argentDePoche.core.generateData;

import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.movementReason.AvailableReasonMovement;

import java.util.List;

public class GenerateReasonMovement {

  public List<AvailableReasonMovement> generate()  {
    AvailableReasonMovement availableReasonMovement = new AvailableReasonMovement(
            "fakeName",
            "fakeCode",
            "fakeAddtionCode",
            "fakeRemoveCode");
    return List.of(availableReasonMovement);
  }
}
