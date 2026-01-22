package com.ctoutweb.argenDePoche.infra.repository.dto;

import org.springframework.data.relational.core.mapping.Column;

import java.util.List;

/**
 * Données sur un compte d'une famille issue de la base de donnée
 *
 * @param familyAccountId L'idnetifiant technique du compte famille
 * @param familyName Le nom de la famille
 * @param parentIds Un tableau des identifiants techniques des parents
 * @param childAccountIds Un tableau des identifiants techniques des comptes d'argnets de poche
 */
public record FamilyAccountProjection(
        @Column("family_account_id")
        Long familyAccountId,

        @Column("family_name")
        String familyName,

        @Column("parent_ids")
        List<Long> parentIds,

        @Column("child_account_ids")
        List<Long> childAccountIds) {
}
