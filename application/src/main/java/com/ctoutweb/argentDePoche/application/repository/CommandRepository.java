package com.ctoutweb.argentDePoche.application.repository;

import com.ctoutweb.argentDePoche.application.port.NextChildAccountIdentities;
import com.ctoutweb.argentDePoche.application.port.NextFamilyAccountIdentities;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccount;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccount;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface CommandRepository {

    /**
     * Chargement des données d'argent de poche d'un enfant
     *
     * @param childMoneyAccountId Identifiant du compte
     * @param periodStart Le debut de la période
     * @param periodEnd Fin de la pépriode
     *
     * @return Les données du compte de l'enfant
     */
    Mono<ChildMoneyAccount> loadChildMoneyAccountAggregate(ChildMoneyAccountIdentity childMoneyAccountId, LocalDate periodStart, LocalDate periodEnd);

    /**
     * Chargement de compte famille à partir de l'identitifiant de parent faison l'action
     *
     * @param parent Le parent faisant l'action
     *
     * @return Le compte familiale lié au parent
     */
    Mono<FamilyAccount> loadFamilyAccountFromParent(ParentIdentity parent);

    /**
     * Mise à jour d'un compte argent de poche d'un enfant
     *
     * @param updatedChildAccount Les données du compte argent de poche
     *
     * @return L'identifiant du compte de l'enfant
     */
    Mono<ChildMoneyAccountIdentity> updateChildMoneyAccount(ChildMoneyAccount updatedChildAccount);

    /**
     * Création d'un compte argent de poche
     *
     * @param childMoneyAccountToBeCreated Les données de création
     *
     * @return L'identifiant du compte de l'enfant
     */
    Mono<ChildMoneyAccountIdentity> createChildMoneyAccount(ChildMoneyAccount childMoneyAccountToBeCreated, FamilyAccount familyAccount);

    /**
     * Creation d'un compte familiale
     *
     * @param familyAccount Les données du compte
     *
     * @return L'identifiant du compte familale
     */
    Mono<FamilyAccountIdentity> createFamilyAccount(FamilyAccount familyAccount, ParentIdentity parentCreatingFamilyAccount);
}
