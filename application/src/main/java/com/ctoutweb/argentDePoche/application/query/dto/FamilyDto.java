package com.ctoutweb.argentDePoche.application.query.dto;

import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.family.FamilyIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;

import java.util.List;

/**
 * Renvoie des données d'une famille
 *
 * @param familyParentIdentities Parents de la famille
 * @param familyName Nom de la famille
 * @param familyChildrenDtos Les enfant de la famille
 */
public record FamilyDto(
        List<ParentIdentity> familyParentIdentities,
        FamilyIdentity familyIdentity,
        String familyName,
        List<FamilyChildDto> familyChildrenDtos) {

}
