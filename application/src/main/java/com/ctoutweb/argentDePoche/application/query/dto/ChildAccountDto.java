package com.ctoutweb.argentDePoche.application.query.dto;

import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.child.ChildIdentity;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Renvoie les données du compte d'argent de poche d'un enfant
 */
public record ChildAccountDto(
        ChildMoneyAccountIdentity childAccountIdentity,
        String imageRandomName,
        ChildIdentity childIdentity,
        String childName,
        BigDecimal remainingMoney,
        BigDecimal moneyAtPeriodStart,
        String periodName,
        LocalDate actualDate,
        LocalDate startPeriodDate,
        LocalDate endPeriodDate) {
}
