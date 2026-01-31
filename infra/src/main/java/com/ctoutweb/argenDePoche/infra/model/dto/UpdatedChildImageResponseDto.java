package com.ctoutweb.argenDePoche.infra.model.dto;

public record UpdatedChildImageResponseDto(long childAccountId, String newImageName, String oldImageName) {

}
