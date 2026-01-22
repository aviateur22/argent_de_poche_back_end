package com.ctoutweb.argenDePoche.infra.repository.dto;

import org.springframework.data.relational.core.mapping.Column;

/**
 * Projection pour la génération des identifiants
 * servant a créer un nouveau compte d'argent de poche
 *
 * @param nextChildId
 * @param nextChildMoneyAccountId
 */
public record NextChilAccountGeneratedProjection(
        @Column("next_child_id")
        Long nextChildId,

        @Column("next_child_money_account_id")
        Long nextChildMoneyAccountId) {
}
