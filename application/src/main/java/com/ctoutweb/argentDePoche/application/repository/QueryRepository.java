package com.ctoutweb.argentDePoche.application.repository;

import com.ctoutweb.argentDePoche.application.query.dto.ChildAccountDto;
import com.ctoutweb.argentDePoche.application.query.dto.FamilyChildDto;
import com.ctoutweb.argentDePoche.application.query.dto.FamilyInformationDto;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface QueryRepository {

    /**
     * Récuperation des comptes d'enfants associé au une compte de famille
     *
     * @param familyAccountIdentity Le Compte famililae
     *
     * @return Liste des données de compte d'enfant
     */
    Flux<FamilyChildDto> loadChildAccountsByFamily(FamilyAccountIdentity familyAccountIdentity);

    /**
     * Récupération des données d'un compte d'argent de poche
     *
     * @param childMoneyAccountIdentity Le compte d'argent qui doit être récupéré
     *
     * @return ChildAccountDto
     */
    Mono<ChildAccountDto> loadChildMoneyAccount(ChildMoneyAccountIdentity childMoneyAccountIdentity);

    /**
     * Récupération des compte de familles  associé a un parent
     *
     * @param parent Le parent
     *
     * @return Une liste des informations de compte de famille associé a un parent
     */
    Flux<FamilyInformationDto> loadFamilyAccountsByParent(ParentIdentity parent);

}
