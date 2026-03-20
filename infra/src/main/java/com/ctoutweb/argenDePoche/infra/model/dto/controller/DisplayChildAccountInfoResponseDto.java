package com.ctoutweb.argenDePoche.infra.model.dto.controller;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Dto permattant d'afficher les données d'un compte d'argent de poche
 * Ces données sont affichable sans qu'on soit authentifié
 */
public record DisplayChildAccountInfoResponseDto(
    long childAccountIdentity,
    String imageRandomName,
    String childName,
    BigDecimal remainingMoney,
    BigDecimal moneyAtPeriodStart,
    LocalDate actualDate,
    LocalDate startPeriodDate,
    LocalDate endPeriodDate,
    String message) {
}
