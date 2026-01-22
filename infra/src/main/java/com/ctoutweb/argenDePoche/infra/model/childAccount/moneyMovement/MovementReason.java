package com.ctoutweb.argenDePoche.infra.model.childAccount.moneyMovement;

import com.ctoutweb.argenDePoche.infra.exception.BalanceReasonException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum MovementReason {
    HELP_IN_HOUSE("H"),
    ROOM_STATE("S"),
    CHILD_BEHAVIOR("B"),
    SCHOOL_HOMEWORK("W"),
    MEAL("M");

    private static final Logger LOGGER = LogManager.getLogger();
    private final String reasonCode;

    private MovementReason(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public static MovementReason findBalanceReason(String reasonCode) throws BalanceReasonException {
        LOGGER.info(() -> String.format("Raison de l'ajout d'une nouvelle balance monétaire à l'argent de poche: %s", reasonCode));

        for(MovementReason balanceReason: MovementReason.values()) {
            if(balanceReason.reasonCode.equalsIgnoreCase(reasonCode))
                return balanceReason;
        }
        throw new BalanceReasonException("La raison pour l'ajout d'une nouvelle balance monétaire n'est pas valide");

    }


}
