package com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.calendar;

import com.ctoutweb.argentDePoche.core.domain.exception.CalendarException;
import com.ctoutweb.argentDePoche.core.domain.exception.CalendarUnvalidException;

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

    public static PeriodSubscription findPeriodSubscription(String subscription) {
        for(PeriodSubscription periodSubscription : PeriodSubscription.values()) {
            if(subscription.equalsIgnoreCase(periodSubscription.periodSubscriptionText))
                return periodSubscription;
        }
        throw new CalendarUnvalidException("Periode d'argent de poche non valide");
    }
}
