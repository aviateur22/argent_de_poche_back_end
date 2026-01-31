package com.ctoutweb.argenDePoche.infra.model.dto.controller;

public record CreateChildAccountRequestDto(long parentId, String childName) {
}
