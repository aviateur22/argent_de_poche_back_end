package com.ctoutweb.argentDePoche.application.generateData;

import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account.ChildMoney;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account.MoneyMovement;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account.MovementActionType;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.remainingMoney.Devise;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.remainingMoney.RemainingMoney;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.Parent;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.util.List;

public class GenerateChildMoney {

    private final GenerateMovementMoney generateMovementMoney;

    public GenerateChildMoney(Parent parent) {
        this.generateMovementMoney = new GenerateMovementMoney(parent.parentIdentity());
    }

    /**
     * Génération de données pour une periode de 1 mois
     *
     * @param initialMoneyAtPeriodStart Argent de poche en debut de période
     * @param totalWeeklyMovementMoneyPrice Montant des mouvements d'argent pour la semaine en cours
     * @param totalMonthlyMovementMoneyPrice Montant des mouvement d'argent pour le mois en cours
     *
     * @return ChildMoney sur 1 mois
     */
    public ChildMoney generateMonthly(
            BigDecimal initialMoneyAtPeriodStart,
            BigDecimal totalMonthlyMovementMoneyPrice,
            BigDecimal totalWeeklyMovementMoneyPrice) {
        List<MoneyMovement> childMonthlyMoneyMovements = generateMovementMoney.generateMonthlyMoneyMovements(totalMonthlyMovementMoneyPrice, totalWeeklyMovementMoneyPrice);

        // Controle
        BigDecimal totalPriceToControl = childMonthlyMoneyMovements.stream().map(movement -> {
            BigDecimal price = movement.fluctuationPrice();
            return movement.action() == MovementActionType.ADD_MONEY ?
                    price
                    : price.negate();

        }).reduce(BigDecimal.ZERO, BigDecimal::add);

        Assertions.assertEquals(0, totalMonthlyMovementMoneyPrice.compareTo(totalPriceToControl));

        RemainingMoney remainingMoney = calculateRemainingMoney(childMonthlyMoneyMovements, initialMoneyAtPeriodStart);
        return new ChildMoney(
                initialMoneyAtPeriodStart,
                remainingMoney
        );
    }

    private RemainingMoney calculateRemainingMoney(List<MoneyMovement> childMoneyMovements, BigDecimal initialMoneyAtPeriodStart) {
        BigDecimal balancePrice = childMoneyMovements
                .stream()
                .map(balance -> balance.action().equals(MovementActionType.ADD_MONEY) ?
                        balance.fluctuationPrice()
                        : balance.fluctuationPrice().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal remainingMoney = balancePrice.compareTo(BigDecimal.ZERO) > 0 ?
                initialMoneyAtPeriodStart.add(balancePrice)
                : initialMoneyAtPeriodStart.subtract(balancePrice);

        if(remainingMoney.compareTo(BigDecimal.ZERO) < 0)
            remainingMoney = BigDecimal.ZERO;

        if(remainingMoney.compareTo(initialMoneyAtPeriodStart) > 0)
            remainingMoney = initialMoneyAtPeriodStart;

        return new RemainingMoney(remainingMoney, Devise.EUR);
    }
}
