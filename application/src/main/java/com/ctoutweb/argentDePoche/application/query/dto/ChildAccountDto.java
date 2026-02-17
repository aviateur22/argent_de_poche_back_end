package com.ctoutweb.argentDePoche.application.query.dto;

import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.child.ChildIdentity;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.movementReason.AvailableReasonMovement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Renvoie les données du compte d'argent de poche d'un enfant
 */
public record ChildAccountDto(
        ChildMoneyAccountIdentity childAccountIdentity,
        ChildIdentity childIdentity,
        String childName,
        String imageRandomName,
        BigDecimal moneyAtPeriodStart,
        BigDecimal remainingMoney,
        LocalDate actualDate,
        LocalDate startPeriodDate,
        LocalDate endPeriodDate,
        String periodName,
        List<AvailableReasonMovement> availableReasonMovements) {
}
