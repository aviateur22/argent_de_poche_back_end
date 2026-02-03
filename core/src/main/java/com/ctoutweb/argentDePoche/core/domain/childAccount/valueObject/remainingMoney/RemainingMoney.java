package com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.remainingMoney;

import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account.MovementActionType;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account.MoneyMovement;

import java.math.BigDecimal;

public record RemainingMoney(BigDecimal remainingMoney, Devise devise) {

    private RemainingMoney with(BigDecimal updateRemainingMoney) {
        return new RemainingMoney(updateRemainingMoney,this.devise);
    }

    /**
     * Mise a jour de l'argent de poche restant lors de l'ajout d'un mouvement d'agent
     *
     * @param childMoneyAtPeriodStart L'argent de poche en debut de periode
     * @param addMoneyBalance Dernier mouvement d'argent
     *
     * @return L'argent de poche restant mis à jour
     */
    public RemainingMoney updateRemainingMoneyOnMovementMoneyAdd( BigDecimal childMoneyAtPeriodStart, MoneyMovement addMoneyBalance) {
        BigDecimal updateRemainingMoney = addMoneyBalance.action().equals(MovementActionType.ADD_MONEY) ?
                this.remainingMoney.add(addMoneyBalance.fluctuationPrice())
                : this.remainingMoney.subtract(addMoneyBalance.fluctuationPrice());

        if(updateRemainingMoney.compareTo(BigDecimal.ZERO) < 0)
            return with(BigDecimal.ZERO);

        if(updateRemainingMoney.compareTo(childMoneyAtPeriodStart) > 0)
            return with(childMoneyAtPeriodStart);

        return with(updateRemainingMoney);
    }

    /**
     * Vérification de l'argent restant quand lors de la modification de l'argent de poche initial
     *
     * @param moneyAtPeriodStart L'argent de poche initial mise a jour
     *
     * @return RemainingMoney
     */
    public RemainingMoney controlRemainingMoneyWhenMoneyAtPeriodStartChange(BigDecimal moneyAtPeriodStart) {
        if(remainingMoney.compareTo(moneyAtPeriodStart) > 0)
            return with(moneyAtPeriodStart);

        return this;
    }

    /**
     * Réinitialise l'argent restant à la valeur de l'argent disponible en début de période
     *
     * @param moneyAtPeriodStart L'argent disponible en début de periode
     *
     * @return L'argent restant reinitialisé a sa valeur initiale
     */
    public RemainingMoney reinitializeRemainingMoney(BigDecimal moneyAtPeriodStart) {
        return with(moneyAtPeriodStart);
    }
}
