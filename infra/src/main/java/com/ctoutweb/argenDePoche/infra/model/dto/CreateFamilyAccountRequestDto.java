package com.ctoutweb.argenDePoche.infra.model.dto;

public record CreateFamilyAccountRequestDto(
        String email,
        String password,
        String parentName) {
}
