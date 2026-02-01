package com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account;

import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.remainingMoney.RemainingMoney;
import com.ctoutweb.argentDePoche.core.domain.exception.UnvalidMoneyAtPeriodStartException;

import java.math.BigDecimal;

public record ChildMoney(
        BigDecimal childMoneyAtPeriodStart,
        RemainingMoney remainingMoney) {

    public ChildMoney {
        if(childMoneyAtPeriodStart.compareTo(BigDecimal.ZERO) <  0)
            throw new UnvalidMoneyAtPeriodStartException("La valeur de l'argent de poche initial ne peut pas être inérieur à 0 ");

    }

    /**
     * Mise à jour de ChildMoney quand un nouveau mouvement d'argent de poche arrive
     *
     * @param updatedRemainingMoney Argent de poche restant apres mise à jour des mouvement d'argent
     *
     * @return Renvoie les données d'argent mise à jour
     */
    public ChildMoney with(RemainingMoney updatedRemainingMoney) {
        return new ChildMoney(this.childMoneyAtPeriodStart, updatedRemainingMoney);
    }

    /**
     * Mise à jour de ChildMoney quand l'argent de poche disponible en début de mois est modifié
     *
     * @param updatedMoneyAtPeriodStart Nouvel argent de poche en debut de période
     *
     * @return Renvoie les données d'argent mise à jour
     */
    public ChildMoney with(BigDecimal updatedMoneyAtPeriodStart) {
        return new ChildMoney(updatedMoneyAtPeriodStart, this.remainingMoney);
    }

    /**
     * Ajout d'un nouveau mouvement d'argent de poche
     *
     * @param moneyMovementToAdd Le nouveau mouvement d'argent
     *
     * @return Renvoie les données d'argent mise à jour
     */
    public ChildMoney addMoneyMovement(MoneyMovement moneyMovementToAdd) {
        // Mise a jour de l'argent de poche restant
        RemainingMoney updatedRemainingMoney = this.remainingMoney.updateRemainingMoneyOnMovementMoneyAdd(
                this.childMoneyAtPeriodStart,
                moneyMovementToAdd);
        return with(updatedRemainingMoney);
    }

    /**
     * Modification de l'argent de poche disponible en début de période
     * L'argent restant et les mouvement d'argent ne sont pas impacté par cette modification
     *
     * @param updatedMoneyAtPeriodStart Le nouveau argent de poche disponible
     *
     * @return Renvoie les données d'argent mise à jour
     */
    public ChildMoney updateMoneyAtPeriodStart(BigDecimal updatedMoneyAtPeriodStart) {
        if(updatedMoneyAtPeriodStart == null)
            throw new UnvalidMoneyAtPeriodStartException("Le nouvel argent de poche est obligatoire");

        if(updatedMoneyAtPeriodStart.compareTo(childMoneyAtPeriodStart) == 0)
            throw new UnvalidMoneyAtPeriodStartException("Sélectionner un nouvel argent de poche différent de l'ancien");

        if(updatedMoneyAtPeriodStart.compareTo(BigDecimal.ZERO) == 0)
            throw new UnvalidMoneyAtPeriodStartException("Le nouvel argent de poche ne peut pas être de 0");

        return with(updatedMoneyAtPeriodStart);
    }
//
//    /**
//     * Filtre les mouvement d'argent de poche sur une période de 1 semaine iso 1 Mois
//     *
//     * @param periodStartDate Date de debut de la semaine
//     * @param periodEndDate Date de la fin de la semaine
//     *
//     * @return Renvoie les données d'argent mise à jour
//     */
//    public ChildMoney weekPeriodSubscription(LocalDate periodStartDate, LocalDate periodEndDate) {
//        List<MoneyMovement> balancesInPeriod = this.moneyMovements
//        .stream()
//        .filter(moneyBalance -> {
//            var occuredDate = moneyBalance.occurredAt().toLocalDate();
//            return !occuredDate.isBefore(periodStartDate) && !occuredDate.isAfter(periodEndDate);
//        })
//        .toList();
//
//        // Mise a jour de l'argent de poche restant
//        RemainingMoney updatedRemainingMoney = this.remainingMoney.updateRemainingMoney(
//                this.childMoneyAtPeriodStart,
//                balancesInPeriod
//        );
//
//        return with(balancesInPeriod, updatedRemainingMoney);
//    }

    /**
     * Réinitialise l'argent de poche pour une nouvelle période
     *
     * @return Renvoie les données d'argent de poche réinitialisé pour une nouvelle période
     */
    public ChildMoney nextPeriod() {
        RemainingMoney updatedRemainingMoney = this.remainingMoney.nextPeriodReinitialize(this.childMoneyAtPeriodStart);
        return with(updatedRemainingMoney);
    }
}
