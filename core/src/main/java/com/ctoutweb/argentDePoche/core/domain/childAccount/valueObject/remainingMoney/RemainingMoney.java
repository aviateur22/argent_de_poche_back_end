package com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.remainingMoney;

import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account.MovementActionType;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account.MoneyMovement;

import java.math.BigDecimal;
import java.util.List;

public record RemainingMoney(BigDecimal remainingMoney, Devise devise) {

    private RemainingMoney with(BigDecimal updateRemainingMoney) {
        return new RemainingMoney(updateRemainingMoney,this.devise);
    }

    /**
     * Mise a jour de l'argent restant
     *
     * @param childMoneyAtPeriodStart L'argent de poche en debut de periode
     * @param moneyMovements L'ensemble des mouvements d'argent d'un compte qui ne prend pas en compte le mouvement d'argent
     * @param lastReceiveMoneyMovement Dernier mouvement d'argent
     *
     * @return L'argent de poche restant mis à jour
     */
    public RemainingMoney updateRemainingMoney(
            BigDecimal childMoneyAtPeriodStart,
            List<MoneyMovement> moneyMovements,
            MoneyMovement lastReceiveMoneyMovement) {
        if(moneyMovements.isEmpty())
            return this;

        BigDecimal moneyBalancePriceWithoutLastMovement = this.calculateBalancePrice(moneyMovements);

        BigDecimal remainingMoneyWithoutLastMovement = this.calculateRemainingMoney(
                moneyBalancePriceWithoutLastMovement,
                childMoneyAtPeriodStart);

        BigDecimal updateRemainingMoney = lastReceiveMoneyMovement.action() == MovementActionType.ADD_MONEY ?
                remainingMoneyWithoutLastMovement.add(lastReceiveMoneyMovement.fluctuationPrice())
                : remainingMoneyWithoutLastMovement.subtract(lastReceiveMoneyMovement.fluctuationPrice());

        if(updateRemainingMoney.compareTo(BigDecimal.ZERO) < 0)
            return with(BigDecimal.ZERO);

        if(updateRemainingMoney.compareTo(childMoneyAtPeriodStart) > 0)
            return with(childMoneyAtPeriodStart);

        return with(updateRemainingMoney);
    }

    /**
     * Recalcul du l'argent de poche restant suite modification de l'argent de poche initial disponible
     *
     * @param updatedChildMoneyAtPeriodStart Nouvel argent de poche disponible
     * @param childMoneyMovements Ensemble des mouvements d'argent sur la période
     *
     * @return L'argent de poche restant mis à jour
     */
    public RemainingMoney updateRemainingMoney(BigDecimal updatedChildMoneyAtPeriodStart, List<MoneyMovement> childMoneyMovements) {
        if(childMoneyMovements.isEmpty())
            return this;

        BigDecimal moneyBalancePrice = this.calculateBalancePrice(childMoneyMovements);

        BigDecimal updateRemainingMoney = this.calculateRemainingMoney(
                moneyBalancePrice,
                updatedChildMoneyAtPeriodStart);

        return with(updateRemainingMoney);
    }

    /**
     * Réinitialise l'argent restant pour une nouvelle période.
     *
     * @param moneyAtPeriodStart L'argent disponible en début de periode
     *
     * @return L'argent restant reinitialisé a sa valeur initiale
     */
    public RemainingMoney nextPeriodReinitialize(BigDecimal moneyAtPeriodStart) {
        return with(moneyAtPeriodStart);
    }

    /**
     * Calcul la balance totale des mouvements d'argent de poche
     *
     * @param childMoneyMovements Les mouvements d'argent de poche sur une periode
     *
     * @return Le montant total des mouvement d'argent de poche
     */
    private BigDecimal calculateBalancePrice(List<MoneyMovement> childMoneyMovements) {
        return childMoneyMovements
                .stream()
                .map(balance -> balance.action().equals(MovementActionType.ADD_MONEY) ?
                        balance.fluctuationPrice()
                        : balance.fluctuationPrice().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcul de l'argent de poche disponible
     *
     * @param moneyBalancePrice Montant du prix des mouvement d'argent dans la période
     * @param childMoneyAtPeriodStart Argent de poche disponible en debut de periode
     *
     * @return L'argent de poche disponible sans le dernier mouvement d'argent
     */
    private BigDecimal calculateRemainingMoney(BigDecimal moneyBalancePrice, BigDecimal childMoneyAtPeriodStart) {
        BigDecimal actualMoney = childMoneyAtPeriodStart.add(moneyBalancePrice);

        if(actualMoney.compareTo(BigDecimal.ZERO) < 0)
            return BigDecimal.ZERO;

        if(actualMoney.compareTo(childMoneyAtPeriodStart) > 0)
            return childMoneyAtPeriodStart;

        return actualMoney;
    }
}
