package com.ctoutweb.argenDePoche.infra.model.dto;

public record CreateChildAccountRequestDto(long parentId, String childName) {
}
