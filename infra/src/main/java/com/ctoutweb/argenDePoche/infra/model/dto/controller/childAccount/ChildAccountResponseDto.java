package com.ctoutweb.argenDePoche.infra.model.dto.controller.childAccount;

import com.ctoutweb.argenDePoche.infra.model.dto.controller.AvailableMovementReasonDto;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.ResponseMessage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
        List<AvailableMovementReasonDto> availableMovementReasonDtos,
        String message) implements ResponseMessage {
  @Override
  public String getMessage() {
    return message;
  }
}
