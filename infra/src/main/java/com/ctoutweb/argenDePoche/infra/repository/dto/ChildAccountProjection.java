package com.ctoutweb.argenDePoche.infra.repository.dto;

import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Données issue de la requette SQL loadChildAccountQuery.
 * Cet objet restitue les données d'un compte d'argent de poche
 * *
 * @see com.ctoutweb.argenDePoche.infra.repository.query.SqlQuery
 */
public record ChildAccountProjection(
        @Column("childmoneyaccountid")
        Long childMoneyAccountId,

        @Column("childid")
        Long childId,

        @Column("childname")
        String childName,

        @Column("imagerandomname")
        String imageRandomName,

        @Column("periodsubscriptionname")
        String periodSubscriptionName,

        @Column("startperioddate")
        LocalDate startPeriodDate,

        @Column("endperioddate")
        LocalDate endPeriodDate,

        @Column("moneyatperiodstart")
        BigDecimal moneyAtPeriodStart,

        @Column("remainingmoney")
        BigDecimal remainingMoney) {
}
