package com.ctoutweb.argentDePoche.application.api;

import com.ctoutweb.argentDePoche.application.query.dto.FamilyDto;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;
import org.reactivestreams.Publisher;

/**
 * Contrat permettant de gerer les comptes de la familles *
 */
public interface FamilyAccountManager {

    /**
     * Récupération des comptes des enfants d'une famille
     *
     * @param parentIdentity L'identité du parent faisant la demande
     * @param familyAccountId L'identity du compte de la famille
     *
     * @return Les données d'argent de poche de la famille
     */
    Publisher<FamilyDto> loadFamilyAccount(ParentIdentity parentIdentity, FamilyAccountIdentity familyAccountId);
}
