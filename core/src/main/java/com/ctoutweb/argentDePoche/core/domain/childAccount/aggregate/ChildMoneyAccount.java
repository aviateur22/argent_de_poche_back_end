package com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate;

import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.child.ChildIdentity;
import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.childImage.ChildImage;
import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.childImage.ImageExtension;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account.MoneyMovement;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.calendar.SubscriptionCalendar;
import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.child.Child;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account.ChildMoney;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.calendar.PeriodSubscription;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.remainingMoney.Devise;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.remainingMoney.RemainingMoney;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class ChildMoneyAccount {
    private final ChildMoneyAccountIdentity childMoneyAccountId;
    private final Child child;
    private final SubscriptionCalendar calendarSubscription;
    private final ChildMoney childMoney;

    public ChildMoneyAccount(
            ChildMoneyAccountIdentity childMoneyAccountId,
            Child child,
            SubscriptionCalendar calendarManager,
            ChildMoney childMoney) {
        this.childMoneyAccountId = childMoneyAccountId;
        this.child = child;
        this.calendarSubscription = calendarManager;
        this.childMoney = childMoney;
    }

    /**
     * Factory pour initialiser un nouveau compte pour enfant
     *
     * @param childName Le nom de l'enfant
     *
     * @return Le compte de l'enfant initialisé
     */
    public static ChildMoneyAccount createDefaultChildAccount(
            ChildMoneyAccountIdentity childMoneyAccountId,
            ChildIdentity childIdentity,
            String childName,
            String defaultImageName,
            ImageExtension imageExtension) {
        // Image par default à la creation d'un compte
        final String DEFAULT_CHILD_IMAGE = defaultImageName;

        // Devise par default à la creation d'un compte
        final Devise DEFAULT_DEVISE = Devise.EUR;

        // Perdiode par default à la creation d'un compte
        final PeriodSubscription DEFAULT_PERIOD_SUBSCRIPTION = PeriodSubscription.WEEK;

        // Argent de poche initial à la creagtion d'un compte
        final BigDecimal DEFAULT_MONEY = BigDecimal.ZERO;

        // Argent restant à l'initialisation d'un nouveau compte
        final BigDecimal DEFAULT_REMAINING_MONEY = BigDecimal.ZERO;

        ChildImage createdChildImage = ChildImage.create(DEFAULT_CHILD_IMAGE, imageExtension);
        Child createdChild = new Child(childIdentity, childName, createdChildImage);
        SubscriptionCalendar createdSubscriptionCalendar = SubscriptionCalendar.created(LocalDate.now(), DEFAULT_PERIOD_SUBSCRIPTION);
        RemainingMoney createdRemainingMoney = new RemainingMoney(DEFAULT_REMAINING_MONEY, DEFAULT_DEVISE);
        ChildMoney createdChildMoney = new ChildMoney(DEFAULT_MONEY, createdRemainingMoney);

        return new ChildMoneyAccount(childMoneyAccountId, createdChild, createdSubscriptionCalendar, createdChildMoney);
    }

    /**
     * Mise à jour de l'image d'un enfant
     *
     * @param updateRandomImageName La nouvelle image de l'enfant
     *
     * @return Données du compte de l'enfant mis à jour avec sa nouvelle photo
     */
    public ChildMoneyAccount updateChildImage(String updateRandomImageName, ImageExtension imageExtension) {
        Child updateChild = this.child.updateImage(updateRandomImageName, imageExtension);
        return new ChildMoneyAccount(this.childMoneyAccountId, updateChild, this.calendarSubscription, this.childMoney);
    }

    /**
     * Ajout / Suppression d'argent dans le portefeuille d'un enfant
     *
     * @param newMoneyMovementInPeriod Ajout / Suppression d'argent
     *
     * @return Données du compte de l'enfant mise à jour
     */
    public ChildMoneyAccount addMoneyMovement(MoneyMovement newMoneyMovementInPeriod){
        ChildMoney updatedChildMoney = this.childMoney.addMoneyMovement(newMoneyMovementInPeriod);
        return new ChildMoneyAccount(this.childMoneyAccountId, this.child, this.calendarSubscription, updatedChildMoney);
    }

    /**
     * Mise à jour de l'argent de poche disponible en debut de période
     *
     * @param updatedMoneyAtPeriodStart Le nouvel argent de poche
     *
     * @return Données du compte de l'enfant mise à jour
     */
    public ChildMoneyAccount updateInitialMoneyAtPeriodStart(BigDecimal updatedMoneyAtPeriodStart) {
        ChildMoney updatedChildMoney = this.childMoney.updateMoneyAtPeriodStart(updatedMoneyAtPeriodStart);
        return new ChildMoneyAccount(this.childMoneyAccountId, this.child, this.calendarSubscription, updatedChildMoney);
    }

    /**
     * Mise à jour du prenom de l'enfant
     *
     * @param updateChildName Le nouveau prénom de l'enfant
     *
     * @return Données du compte de l'enfant mise à jour
     */
    public ChildMoneyAccount updateChildName(String updateChildName) {
        Child updatedChild = this.child.updateChildName(updateChildName);
        return new ChildMoneyAccount(this.childMoneyAccountId, updatedChild, this.calendarSubscription, this.childMoney);
    }

    /**
     * Creation d'une nouvelle periode de calendrier.
     * Cette création intervient en fin de periode et initialise un nouvel période en:
     * - Créant un nouveau calendrier
     * - Reinitialisé l'argent de poche disponible
     *
     * @return Données du compte de l'enfant mise à jour
     */
    public ChildMoneyAccount initializeNextPeriod(LocalDate activateNextPeriodDate) {
        var oldEndPeriodDay = this.calendarSubscription.endDay();
        var nextCalendarPeriod = this.calendarSubscription.nextCalendarPeriod(activateNextPeriodDate);

        // On ne mets pas à jour les comptes qui ont une periode mensuelle si pas la fin du mois
        if(nextCalendarPeriod.endDay().equals(oldEndPeriodDay))
            return this;

        var updateChildMoney = this.childMoney.nextPeriod();
        return new ChildMoneyAccount(this.childMoneyAccountId, this.child, nextCalendarPeriod, updateChildMoney);
    }

    /**
     * Réinitialisation de l'agent de poche restant
     *
     * @return Données du compte de l'enfant mise à jour avec une valeur de l'argent de poche restant reinitilaisé
     */
    public ChildMoneyAccount reinitializeRemainingMoney() {
        ChildMoney reinitializeRemainingMoney = this.childMoney.reinitializeRemainingMoney();
        return new ChildMoneyAccount(this.childMoneyAccountId, this.child, this.calendarSubscription, reinitializeRemainingMoney);
    }

    public ChildMoneyAccountIdentity getChildMoneyAccountId() {
        return childMoneyAccountId;
    }

    public Child getChild() {
        return child;
    }

    public SubscriptionCalendar getCalendarSubscription() {
        return calendarSubscription;
    }

    public ChildMoney getChildMoney() {
        return childMoney;
    }


}
