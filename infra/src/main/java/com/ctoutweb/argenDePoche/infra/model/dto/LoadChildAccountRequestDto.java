package com.ctoutweb.argenDePoche.infra.model.dto;

public record LoadChildAccountRequestDto(
        long childMoneyAccountId,
        long parentIdentity) {
}
