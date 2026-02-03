package com.ctoutweb.argenDePoche.infra.repository.dto;

import com.ctoutweb.argenDePoche.infra.model.dto.childAccount.moneyMovement.MoneyMovementActionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Mouvement d'argent récupéré en base de données
 *
 * @param fluctuationPrice
 * @param movementActionCode
 * @param movementReasonCode
 * @param movementReasonName
 * @param addBy
 */
public record LoadMoneyMovementProjection(
        BigDecimal fluctuationPrice,
        String movementActionCode,
        String movementReasonCode,
        String movementReasonName,
        LocalDateTime movementAddedAt,
        long addBy) {
}
