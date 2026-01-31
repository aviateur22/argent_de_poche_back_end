package com.ctoutweb.argenDePoche.infra.model.dto;

public record UpdatedChildImageDto(long childAccountId, String newImageName, String oldImageName) {

}
