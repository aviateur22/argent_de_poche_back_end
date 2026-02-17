package com.ctoutweb.argentDePoche.application.api;

import com.ctoutweb.argentDePoche.application.command.dto.UpdatedChildImage;
import com.ctoutweb.argentDePoche.application.query.dto.ChildAccountDto;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.childImage.ImageExtension;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ChildAccountUseCase {
    /**
     * Récupération des données de compte compte d'argent de pache d'un enfant
     *
     * @param parentIdentity Identifiant du parent faisant la demande d'affichage
     * @param childMoneyAccountId Identifiant du compte a charger
     *
     * @return Les données de compte d'argent de pache de l'enfant
     */
    Mono<ChildAccountDto> loadChildAccount(ChildMoneyAccountIdentity childMoneyAccountId, ParentIdentity parentIdentity);

    /**
     * Ajout d'un nouveau compte pour enfant
     *
     * @param parentIdentity L'identité du parent faisant la demande
     * @param childName Le nom de l'enfant associé au nouveau compte
     * @param defaultChildImageName nom de l'image imposé par default à la création du compte
     * @param imageExtension Extension de l'image par default
     *
     * @return Les données d'argent de poche de la famille mise a jour
     */
    Mono<ChildMoneyAccountIdentity> createChildMoneyAccount(
            ParentIdentity parentIdentity,
            String childName,
            String defaultChildImageName,
            ImageExtension imageExtension);

    /**
     * Mise à jour de l'image de l'enfant
     *
     * @param parentIdentity  L'identifiant du parent mettant à jour l"image
     * @param childMoneyAccountId L'identifiant du compte de l'enfant
     * @param imageExtension L'extension de l'image
     *
     * @return Les données de compte d'argent de poche de l'enfant mise a jour
     */
    Mono<UpdatedChildImage> updateChildImage(
            ChildMoneyAccountIdentity childMoneyAccountId,
            ParentIdentity parentIdentity,
            ImageExtension imageExtension);

    /**
     * Ajout d'une balance d'argent positive ou negative d'argent dans le compte d'une enfant
     *
     * @param movementReasonCode  La raison du mouvement d'argent
     * @param movementActionCode L'action d'ajout ou de retrait du mouvement d(argent
     * @param childMoneyAccountId L'identifiant du compte de l'enfant
     *
     * @return Les données de compte d'argent de poche de l'enfant mise a jour
     */
    Mono<ChildMoneyAccountIdentity> addChildMoneyMovement(ChildMoneyAccountIdentity childMoneyAccountId, ParentIdentity parentIdentity, String movementReasonCode, String movementActionCode);

    /**
     * Mise a jour de l'argent de poche disponible en début de période pour une enfant
     *
     * @param updatedInitialMoney Nouvel argent disponible en début de période
     * @param childMoneyAccountId L'identifiant du compte de l'enfant
     *
     * @return  Les données de compte d'argent de poche de l'enfant mise a jour
     */
    Mono<ChildMoneyAccountIdentity> modulateInitialChildMoney(ChildMoneyAccountIdentity childMoneyAccountId, ParentIdentity parentIdentity, BigDecimal updatedInitialMoney);

    /**
     * Mise a jour du prénom de l'enfant
     *
     * @param updatedChildName Le nouveau nom de l'enfant
     * @param childMoneyAccountId L'identifiant du compte de l'enfant
     *
     * @return  Les données de compte d'argent de poche de l'enfant mise a jour
     */
    Mono<ChildMoneyAccountIdentity> updateChildName(ChildMoneyAccountIdentity childMoneyAccountId,  ParentIdentity parentIdentity, String updatedChildName);

    /**
     * Génération du nouveau calendrier et réinitialisation de l'argent de poche pour la nouvelle période
     *
     * @return True
     */
    Mono<Boolean> initializeNextCalendarPeriod();

    /**
     * Reinitialisation de l'argent de poche restant
     *
     * @return L'identifiant du compte d'argent de poche mis a jour
     */
    Mono<ChildMoneyAccountIdentity> reinitializeRemainingMoney(
            ChildMoneyAccountIdentity childMoneyAccountId,
            ParentIdentity parentUpdatedChildAccount);

    /**
     * Récupération de l'image de l'enfant
     *
     * @param childMoneyAccountId
     * @param parentUpdatedChildAccount
     *
     * @return Renvoie L'extension de l'image
     */
    Mono<ImageExtension> streamChildImage(
            ChildMoneyAccountIdentity childMoneyAccountId,
            ParentIdentity parentUpdatedChildAccount);

}
