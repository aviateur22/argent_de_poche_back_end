package com.ctoutweb.argentDePoche.application.query.dto.query;

import com.ctoutweb.argentDePoche.application.query.Query;
import com.ctoutweb.argentDePoche.application.query.dto.FamilyDto;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;

public record LoadFamilyAccountQuery(ParentIdentity parentIdentity) implements Query<FamilyDto> {
    /**
     * Instancie LoadFamilyAccountQuery
     *
     * @param parentIdentity  Identity du parent faisant le chargement
     *
     * @return Nouvelle instance
     */
    public static LoadFamilyAccountQuery create(ParentIdentity parentIdentity) {
        return new LoadFamilyAccountQuery(parentIdentity);
    }
}
