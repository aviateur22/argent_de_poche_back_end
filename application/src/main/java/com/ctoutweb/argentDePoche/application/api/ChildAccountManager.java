package com.ctoutweb.argentDePoche.application.api;

import com.ctoutweb.argentDePoche.application.port.AddMoneyMovement;
import com.ctoutweb.argentDePoche.application.port.ImageResource;
import com.ctoutweb.argentDePoche.application.query.dto.ChildAccountDto;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ChildAccountManager {
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
     * @param imagePath Le path par defaut de l'image de l'enfant
     *
     * @return Les données d'argent de poche de la famille mise a jour
     */
    Mono<ChildMoneyAccountIdentity> createChildMoneyAccount(ParentIdentity parentIdentity, String childName, String imagePath);

    /**
     * Mise à jour de l'image de l'enfant
     *
     * @param updatedImage la nouvelle image
     * @param childMoneyAccountId L'identifiant du compte de l'enfant
     *
     * @return Les données de compte d'argent de poche de l'enfant mise a jour
     */
    Publisher<ChildMoneyAccountIdentity> updateChildImage(ChildMoneyAccountIdentity childMoneyAccountId, ImageResource updatedImage, ParentIdentity parentIdentity);

    /**
     * Ajout d'une balance d'argent positive ou negative d'argent dans le compte d'une enfant
     *
     * @param addMoneyMovement  La nouvel balance d'argent a ajouter
     * @param childMoneyAccountId L'identifiant du compte de l'enfant
     *
     * @return Les données de compte d'argent de poche de l'enfant mise a jour
     */
    Publisher<ChildMoneyAccountIdentity> addChildMoneyMovement(ChildMoneyAccountIdentity childMoneyAccountId, AddMoneyMovement addMoneyMovement, ParentIdentity parentIdentity);

    /**
     * Mise a jour de l'argent de poche disponible en début de période pour une enfant
     *
     * @param updatedInitialMoney Nouvel argent disponible en début de période
     * @param childMoneyAccountId L'identifiant du compte de l'enfant
     *
     * @return  Les données de compte d'argent de poche de l'enfant mise a jour
     */
    Publisher<ChildMoneyAccountIdentity> modulateInitialChildMoney(ChildMoneyAccountIdentity childMoneyAccountId, BigDecimal updatedInitialMoney, ParentIdentity parentIdentity);

    /**
     * Mise a jour du prénom de l'enfant
     *
     * @param updatedChildName Le nouveau nom de l'enfant
     * @param childMoneyAccountId L'identifiant du compte de l'enfant
     *
     * @return  Les données de compte d'argent de poche de l'enfant mise a jour
     */
    Publisher<ChildMoneyAccountIdentity> updateChildName(ChildMoneyAccountIdentity childMoneyAccountId, String updatedChildName, ParentIdentity parentIdentity);

    /**
     * Génération du nouveau calendrier et réinitialisation de l'argent de poche pour la nouvelle période
     *
     * @return True
     */
    Mono<Boolean> initializeNextCalendarPeriod();
}
