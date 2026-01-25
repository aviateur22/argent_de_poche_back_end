package com.ctoutweb.argentDePoche.application.query.dto;

import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccount;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.family.FamilyIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;

import java.util.List;

/**
 * Renvoie des données d'une famille
 *
 * @param familyName Nom de la famille
 * @param familyChildrenDtos Les enfant de la famille
 */
public record FamilyDto(
        FamilyAccountIdentity familyAccountIdentity,
        String familyName,
        List<FamilyChildDto> familyChildrenDtos) {

}
