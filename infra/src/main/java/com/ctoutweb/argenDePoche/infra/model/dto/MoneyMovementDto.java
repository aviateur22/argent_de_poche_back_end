package com.ctoutweb.argenDePoche.infra.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MoneyMovementDto(
        long moneyReasonIdByChildAccount,
        long parentAddingMovement,
        BigDecimal fluctuationPrice,
        LocalDateTime movementAddingAt,
        String movementReasonCode,
        String movementReasonName) {
}
