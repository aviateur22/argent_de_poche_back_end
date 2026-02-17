package com.ctoutweb.argenDePoche.infra.repository.dto;


import org.springframework.data.relational.core.mapping.Column;

/**
 * Récupération des information sur un movement
 * @param reasonName La raison en text
 * @param reasonCode Le code associé a cette raison
 */
public record ReasonMovementProjection(
        @Column("movement_name")
        String reasonName,

        @Column("movement_code")
        String reasonCode) {
}
