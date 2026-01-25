package com.ctoutweb.argenDePoche.infra.repository.dto;

import org.springframework.data.relational.core.mapping.Column;

import java.util.List;

/**
 * Cet objet restitue les informations sur un compte de famille
 * Données issue de la requette SQL familyAccountQuery.
 *
 * @see com.ctoutweb.argenDePoche.infra.repository.query.SqlQuery
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
