package com.ctoutweb.argenDePoche.infra.model.dto.controller;

public record ReinitializeRemainingMoneyDto(
        Long parentId,
        Long childAccountId) {
}
