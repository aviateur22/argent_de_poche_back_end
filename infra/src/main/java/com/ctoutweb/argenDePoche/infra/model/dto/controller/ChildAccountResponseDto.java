package com.ctoutweb.argenDePoche.infra.model.dto.controller;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ChildAccountResponseDto(
        long childAccountIdentity,
        long childIdentity,
        String imageRandomName,
        String childName,
        BigDecimal remainingMoney,
        BigDecimal moneyAtPeriodStart,
        String periodName,
        LocalDate actualDate,
        LocalDate startPeriodDate,
        LocalDate endPeriodDate,
        String message) implements ResponseMessage {
  @Override
  public String getMessage() {
    return message;
  }
}
