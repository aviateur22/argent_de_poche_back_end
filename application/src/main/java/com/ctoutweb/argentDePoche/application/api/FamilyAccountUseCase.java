package com.ctoutweb.argentDePoche.application.api;

import com.ctoutweb.argentDePoche.application.query.dto.FamilyDto;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;
import reactor.core.publisher.Mono;

/**
 * Contrat permettant de gerer les comptes de la familles *
 */
public interface FamilyAccountUseCase {

    /**
     * Récupération des comptes des enfants d'une famille
     *
     * @param parentIdentity L'identité du parent faisant la demande
     *
     * @return Les données d'argent de poche de la famille
     */
    Mono<FamilyDto> loadFamilyAccount(ParentIdentity parentIdentity);


    /**
     * Creration d'un nouveau compte familliale
     *
     * @param parentIdentity L'identité du parent faisant la création du compte.
     *                       Issue de l'enregistrement MDP et Email
     * @param familyName Le nom de famille du parent
     *
     * @return L'identifiant du compte familliale qui est créé
     */
    Mono<FamilyAccountIdentity> createFamilyAccount(ParentIdentity parentIdentity, String familyName);


}
