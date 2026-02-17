package com.ctoutweb.argenDePoche.infra.model.dto.controller;

/**
 * Regroupe les informations relatif sur la raison d'un mouvement d'argent
 *
 * @param reasonName La raison en text
 * @param reasonCode Le code de la raison
 * @param addMoneyActionCode Le code de l'action pour ajouter
 * @param removeMoneyActionCode Le code de l'action pour supprimer
 */
public record AvailableMovementReasonDto(
        String reasonName,
        String reasonCode,
        String addMoneyActionCode,
        String removeMoneyActionCode) {
}
