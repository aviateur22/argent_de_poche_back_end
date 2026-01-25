package com.ctoutweb.argenDePoche.infra.repository.dto;

import java.util.List;

/**
 * Chargement des données de la famille pour un parent
 */
public record LoadFamilyAccountProjection(
        Long familyAccountId,
        String familyName,
        String childs) {
}
