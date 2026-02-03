package com.ctoutweb.argenDePoche.infra.model.dto.controller;

import java.math.BigDecimal;

public record UpdateChildMoneyAtPeriodStartRequestDto(
        long parentId,
        long childAccountId,
        BigDecimal updatedMoneyAtPeriodStart) {
}
