package com.ctoutweb.argentDePoche.application.query.dto;

import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Renvoie les données du compte d'argent de poche d'un enfant
 */
public record ChildAccountDto(
        ChildMoneyAccountIdentity childAccountIdentity,
        String imageRandomName,
        String imageRegisterPath,
        String childName,
        BigDecimal remainingMoney,
        BigDecimal MoneyAtPeriodStart,
        String periodName,
        LocalDate actualDate,
        LocalDate startPeriodDate,
        LocalDate endPeriodDate) {
}
