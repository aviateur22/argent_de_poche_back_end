package com.ctoutweb.argenDePoche.infra.model.dto.controller;

public record CreateFamilyAccountRequestDto(
        String email,
        String password,
        String familyName,
        String parentName) {
}
