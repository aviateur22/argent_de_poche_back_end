package com.ctoutweb.argenDePoche.infra.model.childAccount.calendar;

import com.ctoutweb.argenDePoche.infra.exception.PeriodException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum PeriodSubscription {
    MONTH("month"),
    WEEK("week");

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Données recu du client est mapper en donnée de l'infra
     */
    private final String periodSubscriptionFromClient;

    private PeriodSubscription(String periodSubscriptionFromClient) {
        this.periodSubscriptionFromClient = periodSubscriptionFromClient;
    }

    public static PeriodSubscription findPeriodSubscription(String periodSubscriptionFromClient) throws PeriodException {
        LOGGER.info(() -> String.format("Periode de gestion de l'argent: %s", periodSubscriptionFromClient));

        for(PeriodSubscription periodSubscription: PeriodSubscription.values()) {
            if(periodSubscription.periodSubscriptionFromClient.equalsIgnoreCase(periodSubscriptionFromClient))
                return periodSubscription;
        }
        throw new PeriodException("La période de gestion de l'argent de poche n'est pas valide");
    }


}
