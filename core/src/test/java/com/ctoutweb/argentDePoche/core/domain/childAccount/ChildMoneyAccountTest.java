package com.ctoutweb.argentDePoche.core.domain.childAccount;

import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccount;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.child.Child;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account.MovementActionType;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account.MovementReason;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account.ChildMoney;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account.MoneyMovement;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.calendar.PeriodSubscription;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.calendar.SubscriptionCalendar;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.remainingMoney.Devise;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.remainingMoney.RemainingMoney;
import com.ctoutweb.argentDePoche.core.domain.exception.ChildException;
import com.ctoutweb.argentDePoche.core.domain.exception.ChildImageException;
import com.ctoutweb.argentDePoche.core.domain.exception.ChildMoneyException;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.Parent;
import com.ctoutweb.argentDePoche.core.generateData.GenerateCalendar;
import com.ctoutweb.argentDePoche.core.generateData.GenerateChild;
import com.ctoutweb.argentDePoche.core.generateData.GenerateChildMoney;
import com.ctoutweb.argentDePoche.core.generateData.GenerateParent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChildMoneyAccountTest {

    @Test
    void should_load_week_account() {
        /**
         * Given
         */
        BigDecimal monthlyMovementPrice = BigDecimal.valueOf(-1.2);
        BigDecimal weeklyMovementPrice = BigDecimal.valueOf(1.1);
        BigDecimal moneyAtPeriodStart = BigDecimal.valueOf(2);

        ChildMoneyAccountIdentity childMoneyAccountId = new ChildMoneyAccountIdentity(1L);
        Child child = new GenerateChild().generate();
        Parent parent = new GenerateParent().generate();
        SubscriptionCalendar monthlyCalendar = new GenerateCalendar().generate();

        ChildMoney monthlyChildMoney = new GenerateChildMoney(parent)
                .generateMonthly(moneyAtPeriodStart, monthlyMovementPrice, weeklyMovementPrice);

        ChildMoneyAccount monthlyAccount = new ChildMoneyAccount(childMoneyAccountId, child, monthlyCalendar, monthlyChildMoney);

        /**
         * When
         */
        ChildMoneyAccount weeklyAccount = monthlyAccount.loadWeekPeriodSubscription();

        /**
         * Then
         */
        BigDecimal actualPrice = weeklyAccount
                .getChildMoney()
                .moneyMovements()
                .stream()
                .map(moneyMovement -> {
                    BigDecimal movementPrice = moneyMovement.fluctuationPrice();
                    return MovementActionType.ADD_MONEY == moneyMovement.action() ?
                            movementPrice
                            :movementPrice.negate();
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertEquals(0, actualPrice.compareTo(weeklyMovementPrice), "Le montant des mouvements d'argent de la semaine actuel devrait être de " + weeklyMovementPrice + " mais est actuellement de " + actualPrice);
        assertEquals(6, ChronoUnit.DAYS.between(weeklyAccount.getCalendarSubscription().startDay(), weeklyAccount.getCalendarSubscription().endDay()));
        assertEquals(moneyAtPeriodStart, weeklyAccount.getChildMoney().remainingMoney().remainingMoney());

    }

    @Test
    void should_update_child_image() {
        /**
         * Given
         */
        BigDecimal monthlyMovementPrice = BigDecimal.valueOf(1.2);
        BigDecimal weeklyMovementPrice = BigDecimal.valueOf(1.1);
        BigDecimal moneyAtPeriodStart = BigDecimal.valueOf(2);

        ChildMoneyAccountIdentity childMoneyAccountId = new ChildMoneyAccountIdentity(1L);
        Child child = new GenerateChild().generate();
        Parent parent = new GenerateParent().generate();
        SubscriptionCalendar monthlyCalendar = new GenerateCalendar().generate();
        ChildMoney monthlyChildMoney = new GenerateChildMoney(parent).generateMonthly(moneyAtPeriodStart, monthlyMovementPrice, weeklyMovementPrice);

        ChildMoneyAccount monthlyAccount = new ChildMoneyAccount(childMoneyAccountId, child, monthlyCalendar, monthlyChildMoney);

        /**
         * Vérification de la génération mensuelle
         * - Le nom de l'image initial existe
         */
        Assertions.assertFalse(monthlyAccount.getChild().childImage().imageRandomName().isEmpty());

        /**
         * When
         */
        String updatedRandomImageName = "vvvv";
        ChildMoneyAccount updateChildMoneyAccount = monthlyAccount.updateChildImage(updatedRandomImageName);

        /**
         * Then
         * - Le path de l'image existe
         * - Le nom de l'image doit être egal à vvvv
         * - Le prenom ne doit pas avoir changer
         */
        String initialFirstName = child.firstName();
        Assertions.assertNotNull(updateChildMoneyAccount.getChild().childImage().folderPath());
        assertEquals(updatedRandomImageName, updateChildMoneyAccount.getChild().childImage().imageRandomName());
        assertEquals(initialFirstName, updateChildMoneyAccount.getChild().firstName());

    }

    @Test
    void update_child_image_should_throw_when_random_image_name_is_empty() {
        /**
         * Given
         */
        BigDecimal monthlyMovementPrice = BigDecimal.valueOf(1.2);
        BigDecimal weeklyMovementPrice = BigDecimal.valueOf(1.1);
        BigDecimal moneyAtPeriodStart = BigDecimal.valueOf(2);

        ChildMoneyAccountIdentity childMoneyAccountId = new ChildMoneyAccountIdentity(1L);
        Child child = new GenerateChild().generate();
        Parent parent = new GenerateParent().generate();
        SubscriptionCalendar monthlyCalendar = new GenerateCalendar().generate();
        ChildMoney monthlyChildMoney = new GenerateChildMoney(parent).generateMonthly(moneyAtPeriodStart, monthlyMovementPrice, weeklyMovementPrice);

        ChildMoneyAccount monthlyAccount = new ChildMoneyAccount(childMoneyAccountId, child, monthlyCalendar, monthlyChildMoney);

        /**
         * Vérification de la génération mensuelle
         * - Le nom de l'image initial existe
         */
        Assertions.assertFalse(monthlyAccount.getChild().childImage().imageRandomName().isEmpty());

        /**
         * When
         */
        String updatedRandomImageName = "";
        Exception exception = Assertions.assertThrows(ChildImageException.class, () -> monthlyAccount.updateChildImage(updatedRandomImageName));
        assertEquals("Le nom de la nouvelle image ne peut pas être nulle", exception.getMessage());

    }

    @Test
    void update_child_image_should_throw_when_random_image_name_is_null() {
        /**
         * Given
         */
        BigDecimal monthlyMovementPrice = BigDecimal.valueOf(1.2);
        BigDecimal weeklyMovementPrice = BigDecimal.valueOf(1.1);
        BigDecimal moneyAtPeriodStart = BigDecimal.valueOf(2);

        ChildMoneyAccountIdentity childMoneyAccountId = new ChildMoneyAccountIdentity(1L);
        Child child = new GenerateChild().generate();
        Parent parent = new GenerateParent().generate();
        SubscriptionCalendar monthlyCalendar = new GenerateCalendar().generate();
        ChildMoney monthlyChildMoney = new GenerateChildMoney(parent).generateMonthly(moneyAtPeriodStart, monthlyMovementPrice, weeklyMovementPrice);

        ChildMoneyAccount monthlyAccount = new ChildMoneyAccount(childMoneyAccountId, child, monthlyCalendar, monthlyChildMoney);

        /**
         * Vérification de la génération mensuelle
         * - Le nom de l'image initial existe
         */
        Assertions.assertFalse(monthlyAccount.getChild().childImage().imageRandomName().isEmpty());

        /**
         * When
         */
        String updatedRandomImageName = null;
        Exception exception = Assertions.assertThrows(ChildImageException.class, () -> monthlyAccount.updateChildImage(updatedRandomImageName));
        assertEquals("Le nom de la nouvelle image ne peut pas être nulle", exception.getMessage());

    }

    @Test
    void should_update_child_name() {
        /**
         * Given
         */
        BigDecimal monthlyMovementPrice = BigDecimal.valueOf(1.2);
        BigDecimal weeklyMovementPrice = BigDecimal.valueOf(1.1);
        BigDecimal moneyAtPeriodStart = BigDecimal.valueOf(2);

        ChildMoneyAccountIdentity childMoneyAccountId = new ChildMoneyAccountIdentity(1L);
        Child child = new GenerateChild().generate();
        Parent parent = new GenerateParent().generate();
        SubscriptionCalendar monthlyCalendar = new GenerateCalendar().generate();
        ChildMoney monthlyChildMoney = new GenerateChildMoney(parent).generateMonthly(moneyAtPeriodStart, monthlyMovementPrice, weeklyMovementPrice);

        ChildMoneyAccount monthlyAccount = new ChildMoneyAccount(childMoneyAccountId, child, monthlyCalendar, monthlyChildMoney);

        /**
         * Vérification de la génération mensuelle
         * - Le nom de l'enfant existe
         */
        Assertions.assertFalse(monthlyAccount.getChild().firstName().isEmpty());

        /**
         * When
         */
        String updateFirstName = "dfg";
        ChildMoneyAccount updateChildMoneyAccount = monthlyAccount.updateChildName(updateFirstName);

        /**
         * Then
         */
        Assertions.assertNotNull(updateChildMoneyAccount.getChild().childImage().folderPath());
        Assertions.assertNotNull(updateChildMoneyAccount.getChild().childImage().imageRandomName());
        assertEquals(updateFirstName, updateChildMoneyAccount.getChild().firstName());

    }

    @Test
    void update_child_name_should_throw_ChildException_when_firstname_null() {
        /**
         * Given
         */
        BigDecimal monthlyMovementPrice = BigDecimal.valueOf(1.2);
        BigDecimal weeklyMovementPrice = BigDecimal.valueOf(1.1);
        BigDecimal moneyAtPeriodStart = BigDecimal.valueOf(2);

        ChildMoneyAccountIdentity childMoneyAccountId = new ChildMoneyAccountIdentity(1L);
        Child child = new GenerateChild().generate();
        Parent parent = new GenerateParent().generate();
        SubscriptionCalendar monthlyCalendar = new GenerateCalendar().generate();
        ChildMoney monthlyChildMoney = new GenerateChildMoney(parent).generateMonthly(moneyAtPeriodStart, monthlyMovementPrice, weeklyMovementPrice);

        ChildMoneyAccount monthlyAccount = new ChildMoneyAccount(childMoneyAccountId, child, monthlyCalendar, monthlyChildMoney);

        /**
         * Vérification de la génération mensuelle
         * - Le nom de l'enfant existe
         */
        Assertions.assertFalse(monthlyAccount.getChild().firstName().isEmpty());

        /**
         * When
         */
        String updateFirstName = null;
        Exception exception = Assertions.assertThrows(ChildException.class, () -> monthlyAccount.updateChildName(updateFirstName));
        assertEquals("Le prénom ne peux pas être inférieur a 3 charactères", exception.getMessage());

    }

    @Test
    void update_child_name_should_throw_ChildException_when_firstname_to_short() {
        /**
         * Given
         */
        BigDecimal monthlyMovementPrice = BigDecimal.valueOf(1.2);
        BigDecimal weeklyMovementPrice = BigDecimal.valueOf(1.1);
        BigDecimal moneyAtPeriodStart = BigDecimal.valueOf(2);

        ChildMoneyAccountIdentity childMoneyAccountId = new ChildMoneyAccountIdentity(1L);
        Child child = new GenerateChild().generate();
        Parent parent = new GenerateParent().generate();
        SubscriptionCalendar monthlyCalendar = new GenerateCalendar().generate();
        ChildMoney monthlyChildMoney = new GenerateChildMoney(parent).generateMonthly(moneyAtPeriodStart, monthlyMovementPrice, weeklyMovementPrice);

        ChildMoneyAccount monthlyAccount = new ChildMoneyAccount(childMoneyAccountId, child, monthlyCalendar, monthlyChildMoney);

        /**
         * Vérification de la génération mensuelle
         * - Le nom de l'enfant existe
         */
        Assertions.assertFalse(monthlyAccount.getChild().firstName().isEmpty());

        /**
         * When
         */
        String updateFirstName = "dd";
        Exception exception = Assertions.assertThrows(ChildException.class, () -> monthlyAccount.updateChildName(updateFirstName));
        assertEquals("Le prénom ne peux pas être inférieur a 3 charactères", exception.getMessage());

    }

    @Test
    void update_child_name_should_throw_ChildException_when_new_firstname_equal_to_old_firstname() {
        /**
         * Given
         */
        BigDecimal monthlyMovementPrice = BigDecimal.valueOf(1.2);
        BigDecimal weeklyMovementPrice = BigDecimal.valueOf(1.1);
        BigDecimal moneyAtPeriodStart = BigDecimal.valueOf(2);

        ChildMoneyAccountIdentity childMoneyAccountId = new ChildMoneyAccountIdentity(1L);
        Child child = new GenerateChild().generate();
        Parent parent = new GenerateParent().generate();
        SubscriptionCalendar monthlyCalendar = new GenerateCalendar().generate();
        ChildMoney monthlyChildMoney = new GenerateChildMoney(parent).generateMonthly(moneyAtPeriodStart, monthlyMovementPrice, weeklyMovementPrice);

        ChildMoneyAccount monthlyAccount = new ChildMoneyAccount(childMoneyAccountId, child, monthlyCalendar, monthlyChildMoney);

        /**
         * Vérification de la génération mensuelle
         * - Le nom de l'enfant existe
         */
        Assertions.assertFalse(monthlyAccount.getChild().firstName().isEmpty());

        /**
         * When
         */
        String updateFirstName = monthlyAccount.getChild().firstName();
        Exception exception = Assertions.assertThrows(ChildException.class, () -> monthlyAccount.updateChildName(updateFirstName));
        assertEquals("Le prénom de l'enfant doit être différent", exception.getMessage());

    }

    @Test
    void should_update_money_balances_with_a_positive_balance() {
        /**
         * Given
         */
        BigDecimal monthlyMovementPrice = BigDecimal.valueOf(4);
        BigDecimal weeklyMovementPrice = BigDecimal.valueOf(-1);
        BigDecimal moneyAtPeriodStart = BigDecimal.valueOf(2);

        // Le mouvement d'argent sur la semaine doit être négatif
        Assertions.assertTrue(weeklyMovementPrice.compareTo(BigDecimal.ZERO) < 0, "Le mouvement d'argent sur la semaine doit être négatif");

        ChildMoneyAccountIdentity childMoneyAccountId = new ChildMoneyAccountIdentity(1L);
        Child child = new GenerateChild().generate();
        Parent parent = new GenerateParent().generate();
        SubscriptionCalendar monthlyCalendar = new GenerateCalendar().generate();
        ChildMoney monthlyChildMoney = new GenerateChildMoney(parent).generateMonthly(moneyAtPeriodStart, monthlyMovementPrice, weeklyMovementPrice);

        ChildMoneyAccount monthlyAccount = new ChildMoneyAccount(childMoneyAccountId, child, monthlyCalendar, monthlyChildMoney);
        ChildMoneyAccount weeklyAccount = monthlyAccount.loadWeekPeriodSubscription();

        // Vérification argent restant la semaine
        assertEquals(moneyAtPeriodStart.add(weeklyMovementPrice).setScale(2, RoundingMode.UNNECESSARY), weeklyAccount.getChildMoney().remainingMoney().remainingMoney().setScale(2, RoundingMode.UNNECESSARY));

        /**
         * When
         */
        MoneyMovement moneyMovementToAdd = new MoneyMovement(
            new BigDecimal("0.5"),
                MovementActionType.ADD_MONEY,
                MovementReason.CHILD_BEHAVIOR,
                LocalDateTime.now(),
                parent.parentIdentity()
        );

        ChildMoneyAccount updateChildMoneyAccount = weeklyAccount.addMoneyMovement(moneyMovementToAdd);

        /**
         * Then
         */
        // RemainingMoney attendu
        BigDecimal expectedRemainingMoney = weeklyMovementPrice.add(moneyAtPeriodStart).add(moneyMovementToAdd.fluctuationPrice()).setScale(2, RoundingMode.UNNECESSARY);
        assertEquals(expectedRemainingMoney, updateChildMoneyAccount.getChildMoney().remainingMoney().remainingMoney().setScale(2, RoundingMode.UNNECESSARY));
    }

    @Test
    void should_update_money_balances_with_a_negative_balance() {
        /**
         * Given
         */
        BigDecimal monthlyMovementPrice = BigDecimal.valueOf(1.2);
        BigDecimal weeklyMovementPrice = BigDecimal.valueOf(1.1);
        BigDecimal moneyAtPeriodStart = BigDecimal.valueOf(2);

        // Le mouvement d'argent sur la semaine doit être positive
        Assertions.assertFalse(weeklyMovementPrice.compareTo(BigDecimal.ZERO) < 0, "Le mouvement d'argent sur la semaine doit être positif");

        ChildMoneyAccountIdentity childMoneyAccountId = new ChildMoneyAccountIdentity(1L);
        Child child = new GenerateChild().generate();
        Parent parent = new GenerateParent().generate();
        SubscriptionCalendar monthlyCalendar = new GenerateCalendar().generate();
        ChildMoney monthlyChildMoney = new GenerateChildMoney(parent).generateMonthly(moneyAtPeriodStart, monthlyMovementPrice, weeklyMovementPrice);

        ChildMoneyAccount monthlyAccount = new ChildMoneyAccount(childMoneyAccountId, child, monthlyCalendar, monthlyChildMoney);
        ChildMoneyAccount weeklyAccount = monthlyAccount.loadWeekPeriodSubscription();

        /**
         * When
         */
        MoneyMovement moneyMovementToAdd = new MoneyMovement(
                new BigDecimal("0.5"),
                MovementActionType.REMOVE_MONEY,
                MovementReason.CHILD_BEHAVIOR,
                LocalDateTime.now(),
                parent.parentIdentity()
        );

        ChildMoneyAccount updateChildMoneyAccount = weeklyAccount.addMoneyMovement(moneyMovementToAdd);

        /**
         * Then
         */
        BigDecimal expectedRemainingMoney = moneyAtPeriodStart.subtract(moneyMovementToAdd.fluctuationPrice()).setScale(2, RoundingMode.UNNECESSARY);
        expectedRemainingMoney = expectedRemainingMoney.compareTo(moneyAtPeriodStart) > 0 ? moneyAtPeriodStart :  expectedRemainingMoney;

        assertEquals(expectedRemainingMoney, updateChildMoneyAccount.getChildMoney().remainingMoney().remainingMoney().setScale(2, RoundingMode.UNNECESSARY), "L'argent restant après l'ajout d'un mouvement d'argent négatif est faux");
    }

    @Test
    void remaining_money_should_be_1_when_update_money_at_period_start() {
        /**
         * Given
         */
        BigDecimal monthlyMovementPrice = BigDecimal.valueOf(1.2);
        BigDecimal weeklyMovementPrice = BigDecimal.valueOf(1.1);
        BigDecimal moneyAtPeriodStart = BigDecimal.valueOf(2);

        // Le mouvement d'argent sur la semaine doit être positive
        Assertions.assertFalse(weeklyMovementPrice.compareTo(BigDecimal.ZERO) < 0, "Le mouvement d'argent sur la semaine doit être positif");

        ChildMoneyAccountIdentity childMoneyAccountId = new ChildMoneyAccountIdentity(1L);
        Child child = new GenerateChild().generate();
        Parent parent = new GenerateParent().generate();
        SubscriptionCalendar monthlyCalendar = new GenerateCalendar().generate();
        ChildMoney monthlyChildMoney = new GenerateChildMoney(parent).generateMonthly(moneyAtPeriodStart, monthlyMovementPrice, weeklyMovementPrice);

        ChildMoneyAccount monthlyAccount = new ChildMoneyAccount(childMoneyAccountId, child, monthlyCalendar, monthlyChildMoney);
        ChildMoneyAccount weeklyAccount = monthlyAccount.loadWeekPeriodSubscription();

        /**
         * When
         */
        BigDecimal newMoneyAtPeriodStart = BigDecimal.valueOf(5);
        ChildMoneyAccount updatedWeeklyAccount = weeklyAccount.updateInitialMoneyAtPeriodStart(newMoneyAtPeriodStart);

        /**
         * Then
         */
        assertEquals(newMoneyAtPeriodStart, updatedWeeklyAccount.getChildMoney().childMoneyAtPeriodStart());
        assertEquals(weeklyAccount.getChildMoney().moneyMovements(), updatedWeeklyAccount.getChildMoney().moneyMovements());
        assertEquals(weeklyAccount.getChildMoney().remainingMoney().remainingMoney(), updatedWeeklyAccount.getChildMoney().remainingMoney().remainingMoney());
    }

    @Test
    void remaining_money_should_be_throw_when_update_money_is_null_or_0() {
        /**
         * Given
         */
        BigDecimal monthlyMovementPrice = BigDecimal.valueOf(1.2);
        BigDecimal weeklyMovementPrice = BigDecimal.valueOf(3.1);
        BigDecimal moneyAtPeriodStart = BigDecimal.valueOf(4);

        // Le mouvement d'argent sur la semaine doit être positive
        Assertions.assertFalse(weeklyMovementPrice.compareTo(BigDecimal.ZERO) < 0, "Le mouvement d'argent sur la semaine doit être positif");

        ChildMoneyAccountIdentity childMoneyAccountId = new ChildMoneyAccountIdentity(1L);
        Child child = new GenerateChild().generate();
        Parent parent = new GenerateParent().generate();
        SubscriptionCalendar monthlyCalendar = new GenerateCalendar().generate();
        ChildMoney monthlyChildMoney = new GenerateChildMoney(parent).generateMonthly(moneyAtPeriodStart, monthlyMovementPrice, weeklyMovementPrice);

        ChildMoneyAccount monthlyAccount = new ChildMoneyAccount(childMoneyAccountId, child, monthlyCalendar, monthlyChildMoney);
        ChildMoneyAccount weeklyAccount = monthlyAccount.loadWeekPeriodSubscription();
        /**
         * Then
         */
        final BigDecimal newMoneyAtPeriodStart = null;
        Exception exception = Assertions.assertThrows(ChildMoneyException.class, () -> weeklyAccount.updateInitialMoneyAtPeriodStart(newMoneyAtPeriodStart));

        final BigDecimal newMoneyAtPeriodStartAtZero = BigDecimal.valueOf(0);
        exception = Assertions.assertThrows(ChildMoneyException.class, () -> weeklyAccount.updateInitialMoneyAtPeriodStart(newMoneyAtPeriodStartAtZero));
    }

    @Test
    void update_money_at_period_start_and_add_new_positive_money_movement() {
        /**
         * Given
         */
        BigDecimal monthlyMovementPrice = BigDecimal.valueOf(4.2);
        BigDecimal weeklyMovementPrice = BigDecimal.valueOf(1.0);
        BigDecimal moneyAtPeriodStart = BigDecimal.valueOf(4);

        // Le mouvement d'argent sur la semaine doit être positive
        Assertions.assertFalse(weeklyMovementPrice.compareTo(BigDecimal.ZERO) < 0, "Le mouvement d'argent sur la semaine doit être positif");

        ChildMoneyAccountIdentity childMoneyAccountId = new ChildMoneyAccountIdentity(1L);
        Child child = new GenerateChild().generate();
        Parent parent = new GenerateParent().generate();
        SubscriptionCalendar monthlyCalendar = new GenerateCalendar().generate();
        ChildMoney monthlyChildMoney = new GenerateChildMoney(parent).generateMonthly(moneyAtPeriodStart, monthlyMovementPrice, weeklyMovementPrice);

        ChildMoneyAccount monthlyAccount = new ChildMoneyAccount(childMoneyAccountId, child, monthlyCalendar, monthlyChildMoney);
        ChildMoneyAccount weeklyAccount = monthlyAccount.loadWeekPeriodSubscription();

        /**
         * when
         * - Modification de l'argent disponible
         * - Ajout d'un nouveau mouvement d'argent
         */
        final BigDecimal newMoneyAtPeriodStart = BigDecimal.valueOf(3);
        ChildMoneyAccount updatedMoneyStartAccount = weeklyAccount.updateInitialMoneyAtPeriodStart(newMoneyAtPeriodStart);
        MoneyMovement moneyMovementToAdd = new MoneyMovement(
                new BigDecimal("0.5"),
                MovementActionType.ADD_MONEY,
                MovementReason.CHILD_BEHAVIOR,
                LocalDateTime.now(),
                parent.parentIdentity()
        );
        ChildMoneyAccount updatedMovementsAccount = updatedMoneyStartAccount.addMoneyMovement(moneyMovementToAdd);


        /**
         * Then
         */
        BigDecimal moneyAtPeriodStartControl = updatedMovementsAccount.getChildMoney().childMoneyAtPeriodStart();

        BigDecimal expectedRemainingMoney = moneyAtPeriodStartControl.add(moneyMovementToAdd.fluctuationPrice()).setScale(2, RoundingMode.UNNECESSARY);
        expectedRemainingMoney = expectedRemainingMoney.compareTo(moneyAtPeriodStartControl) > 0 ?
                moneyAtPeriodStartControl
                :  expectedRemainingMoney;

        assertEquals(expectedRemainingMoney.setScale(2, RoundingMode.UNNECESSARY), updatedMovementsAccount.getChildMoney().remainingMoney().remainingMoney().setScale(2, RoundingMode.UNNECESSARY), "L'argent restant après l'ajout d'un mouvement d'argent négatif est faux");
    }

    @Test
    void update_money_at_period_start_and_add_new_negative_money_movement() {
        /**
         * Given
         */
        BigDecimal monthlyMovementPrice = BigDecimal.valueOf(4.2);
        BigDecimal weeklyMovementPrice = BigDecimal.valueOf(1.0);
        BigDecimal moneyAtPeriodStart = BigDecimal.valueOf(4);

        // Le mouvement d'argent sur la semaine doit être positive
        Assertions.assertFalse(weeklyMovementPrice.compareTo(BigDecimal.ZERO) < 0, "Le mouvement d'argent sur la semaine doit être positif");

        ChildMoneyAccountIdentity childMoneyAccountId = new ChildMoneyAccountIdentity(1L);
        Child child = new GenerateChild().generate();
        Parent parent = new GenerateParent().generate();
        SubscriptionCalendar monthlyCalendar = new GenerateCalendar().generate();
        ChildMoney monthlyChildMoney = new GenerateChildMoney(parent).generateMonthly(moneyAtPeriodStart, monthlyMovementPrice, weeklyMovementPrice);

        ChildMoneyAccount monthlyAccount = new ChildMoneyAccount(childMoneyAccountId, child, monthlyCalendar, monthlyChildMoney);
        ChildMoneyAccount weeklyAccount = monthlyAccount.loadWeekPeriodSubscription();

        /**
         * when
         * - Modification de l'argent disponible
         * - Ajout d'un nouveau mouvement d'argent
         */
        final BigDecimal newMoneyAtPeriodStart = BigDecimal.valueOf(3);
        ChildMoneyAccount updatedMoneyStartAccount = weeklyAccount.updateInitialMoneyAtPeriodStart(newMoneyAtPeriodStart);
        MoneyMovement moneyMovementToAdd = new MoneyMovement(
                new BigDecimal("0.5"),
                MovementActionType.REMOVE_MONEY,
                MovementReason.CHILD_BEHAVIOR,
                LocalDateTime.now(),
                parent.parentIdentity()
        );
        // Le mouvement d'argent doit être négative
        Assertions.assertTrue(moneyMovementToAdd.action() == MovementActionType.REMOVE_MONEY, "L'ajout du mouvement d'argent doit être de type REMOVE_MONEY");

        ChildMoneyAccount updatedMovementsAccount = updatedMoneyStartAccount.addMoneyMovement(moneyMovementToAdd);


        /**
         * Then
         */
        BigDecimal moneyAtPeriodStartControl = updatedMovementsAccount.getChildMoney().childMoneyAtPeriodStart();

        BigDecimal expectedRemainingMoney = moneyAtPeriodStartControl.subtract(moneyMovementToAdd.fluctuationPrice()).setScale(2, RoundingMode.UNNECESSARY);
        expectedRemainingMoney = expectedRemainingMoney.compareTo(moneyAtPeriodStartControl) > 0 ?
                moneyAtPeriodStartControl
                :  expectedRemainingMoney;

        assertEquals(expectedRemainingMoney.setScale(2, RoundingMode.UNNECESSARY), updatedMovementsAccount.getChildMoney().remainingMoney().remainingMoney().setScale(2, RoundingMode.UNNECESSARY), "L'argent restant après l'ajout d'un mouvement d'argent négatif est faux");
    }

    @Test
    void next_calendar_period_for_week_subscription() {
        /**
         * Given
         */
        ChildMoneyAccountIdentity childMoneyAccountId = new ChildMoneyAccountIdentity(1L);
        Child child = new GenerateChild().generate();

        SubscriptionCalendar monthlyCalendar = new SubscriptionCalendar(
                LocalDate.now(),
                PeriodSubscription.WEEK,
                LocalDate.of(2025, 12, 29),
                LocalDate.of(2026, 1, 4)
        );

        BigDecimal moneyAtPeriodStart = BigDecimal.valueOf(4);
        RemainingMoney remainingMoney = new RemainingMoney(BigDecimal.valueOf(1.0), Devise.EUR);
        ChildMoney monthlyChildMoney = new ChildMoney(moneyAtPeriodStart, List.of(), remainingMoney);

        ChildMoneyAccount childAccount = new ChildMoneyAccount(childMoneyAccountId, child, monthlyCalendar, monthlyChildMoney);

        /**
         * when
         */
        var activateNextPeriodDate = LocalDate.of(2026, 1,21);
        var weekAccountNextPeriod = childAccount.initializeNextPeriod(activateNextPeriodDate);

        /**
         * then
         */
        assertEquals(child.firstName(), weekAccountNextPeriod.getChild().firstName());
        assertEquals(LocalDate.of(2026, 1, 11), weekAccountNextPeriod.getCalendarSubscription().endDay());
        assertEquals(LocalDate.of(2026, 1, 5), weekAccountNextPeriod.getCalendarSubscription().startDay());
        assertEquals(moneyAtPeriodStart, weekAccountNextPeriod.getChildMoney().remainingMoney().remainingMoney());
        assertEquals(moneyAtPeriodStart, weekAccountNextPeriod.getChildMoney().childMoneyAtPeriodStart());
    }

    @Test
    void next_calendar_period_for_month_subscription() {
        /**
         * Given
         */
        ChildMoneyAccountIdentity childMoneyAccountId = new ChildMoneyAccountIdentity(1L);
        Child child = new GenerateChild().generate();

        SubscriptionCalendar monthlyCalendar = new SubscriptionCalendar(
                LocalDate.now(),
                PeriodSubscription.MONTH,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 31)
        );

        BigDecimal moneyAtPeriodStart = BigDecimal.valueOf(4);
        RemainingMoney remainingMoney = new RemainingMoney(BigDecimal.valueOf(1.0), Devise.EUR);
        ChildMoney monthlyChildMoney = new ChildMoney(moneyAtPeriodStart, List.of(), remainingMoney);

        ChildMoneyAccount childAccount = new ChildMoneyAccount(childMoneyAccountId, child, monthlyCalendar, monthlyChildMoney);

        /**
         * when
         */
        var activateNextPeriodDate = LocalDate.of(2026, 1,31);
        var monthAccountNextPeriod = childAccount.initializeNextPeriod(activateNextPeriodDate);

        /**
         * then
         */
        // Prochaine
        assertEquals(child.firstName(), monthAccountNextPeriod.getChild().firstName());
        assertEquals(LocalDate.of(2026, 2, 1), monthAccountNextPeriod.getCalendarSubscription().startDay());
        assertEquals(LocalDate.of(2026, 2, 28), monthAccountNextPeriod.getCalendarSubscription().endDay());
        assertEquals(moneyAtPeriodStart, monthAccountNextPeriod.getChildMoney().remainingMoney().remainingMoney());
        assertEquals(moneyAtPeriodStart, monthAccountNextPeriod.getChildMoney().childMoneyAtPeriodStart());

    }


}
