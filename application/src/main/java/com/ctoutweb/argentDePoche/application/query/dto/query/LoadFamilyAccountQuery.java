package com.ctoutweb.argentDePoche.application.query.dto.query;

import com.ctoutweb.argentDePoche.application.query.Query;
import com.ctoutweb.argentDePoche.application.query.dto.FamilyDto;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;

public record LoadFamilyAccountQuery(
        FamilyAccountIdentity familyAccountId,
        ParentIdentity parentIdentity) implements Query<FamilyDto> {
    /**
     * Instancie LoadFamilyAccountQuery
     *
     * @param familyAccountId Identity du compte de la famille
     * @param parentIdentity  Identity du parent faisant le chargement
     *
     * @return Nouvelle instance
     */
    public static LoadFamilyAccountQuery create(FamilyAccountIdentity familyAccountId, ParentIdentity parentIdentity) {
        return new LoadFamilyAccountQuery(familyAccountId, parentIdentity);
    }
}
