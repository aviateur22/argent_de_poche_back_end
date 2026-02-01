package com.ctoutweb.argenDePoche.infra.model.dto;

import com.ctoutweb.argentDePoche.application.port.AddMoneyMovementReason;

import java.math.BigDecimal;

/**
 * Récuperation des données issue de la base de données
 *
 */
public record AddMoneyMovementReasonDto(
        BigDecimal fluctuationPrice,
        String movementReasonCode,
        String movementReasonName

)  implements AddMoneyMovementReason {

  @Override
  public BigDecimal getFluctuationPrice() {
    return fluctuationPrice;
  }

  @Override
  public String getMovementReasonCode() {
    return movementReasonCode;
  }

  @Override
  public String getMovementReasonName() {
    return movementReasonName;
  }
}
