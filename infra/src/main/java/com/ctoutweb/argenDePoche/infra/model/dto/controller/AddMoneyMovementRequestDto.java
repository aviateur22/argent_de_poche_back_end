package com.ctoutweb.argenDePoche.infra.model.dto.controller;

public record AddMoneyMovementRequestDto(
        long childAccountId,
        long parentId,
        String reasonCode,
        String actionCode) {
}
