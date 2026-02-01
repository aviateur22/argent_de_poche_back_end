package com.ctoutweb.argentDePoche.application.repository;

import com.ctoutweb.argentDePoche.application.port.AddMoneyMovementReason;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccount;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account.MoneyMovement;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccount;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;
import reactor.core.publisher.Mono;

public interface CommandRepository {

    /**
     * Chargement des données d'argent de poche d'un enfant
     * L'aggregat ChildMoneyAccount qui est chargé aura les données Calendar suivant:
     * - De la semaine actuelle si periode de l'argent de poche est  WEEK
     * - Du mois actuel si la periode de l'argent de poche est MONTH
     *
     * @param childMoneyAccountId Identifiant du compte
     *
     * @return Les données du compte de l'enfant
     */
    Mono<ChildMoneyAccount> loadActiveChildAccountAggregate(ChildMoneyAccountIdentity childMoneyAccountId);

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
     * Le compte qui est mis a jour est celui:
     * - De la semaine actuelle si periode de l'argent de poche est  WEEk
     * - Du mois actuel si la periode de l'argent de poche est MONTH
     *
     * @param updatedChildAccount Les données du compte argent de poche
     *
     * @return L'identifiant du compte de l'enfant
     */
    Mono<ChildMoneyAccountIdentity> updateActiveChildMoneyAccount(ChildMoneyAccount updatedChildAccount);

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

    Mono<Boolean> initializeNextPeriod();

    /**
     * Chargement des informationd sur les informations d'un mouvement d'argent
     *
     * @param movementReasonCode Le code de la raison du mouvement d'argent
     * @param childAccountIdentity  L'identifiant du compte d'argent de poche
     *
     * @return AddMoneyMovementReason
     */
    Mono<AddMoneyMovementReason> loadMoneyMovementReasonByCode(String movementReasonCode, ChildMoneyAccountIdentity childAccountIdentity);

    /**
     * Ajout d'un mouvement d'argent
     *
     * @param moneyMovementToAdd Le mouvement d'argent
     * @param childAccountToBeUpdated Le compte d'argent poche qui recoit le mouvement d'argent
     *
     * @return L'identifiant du compte d'argent de poche quia recu le mouvemrnt
     */
    Mono<ChildMoneyAccountIdentity> addMoneyMovement(MoneyMovement moneyMovementToAdd, ChildMoneyAccountIdentity childAccountToBeUpdated);
}
