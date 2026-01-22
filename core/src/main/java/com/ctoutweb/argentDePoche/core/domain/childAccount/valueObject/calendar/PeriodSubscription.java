package com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.calendar;

public enum PeriodSubscription {
    MONTH("month"),
    WEEK("week");

    /**
     * Données recu du client est mapper en donnée de l'infra
     */
    private final String periodSubscriptionText;

    private PeriodSubscription(String periodSubscriptionText) {
        this.periodSubscriptionText = periodSubscriptionText;
    }

    public String getPeriodSubscriptionText() {
        return this.periodSubscriptionText;
    }
}
