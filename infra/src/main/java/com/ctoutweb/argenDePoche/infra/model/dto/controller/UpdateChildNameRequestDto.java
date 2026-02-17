package com.ctoutweb.argenDePoche.infra.model.dto.controller;

/**
 * Dto permettant de mettre à jour le prenom d'un enfant
 *
 * @param parentId
 * @param childAccountId
 * @param updateChildName
 */
public record UpdateChildNameRequestDto(
        long parentId,
        long childAccountId,
        String updateChildName) {
}
