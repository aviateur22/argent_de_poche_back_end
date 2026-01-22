package com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account;

import com.ctoutweb.argentDePoche.core.domain.exception.MoneyMovementException;

public enum MovementReason {
    HELP_IN_HOUSE,
    ROOM_STATE,
    CHILD_BEHAVIOR,
    SCHOOL_HOMEWORK,
    MEAL;

    /**
     * Renvoie la raison du mouvement d'argent
     *
     * @param code recu permettant de renvoyer un MovementReason
     *
     * @return MovementReason
     */
    public static MovementReason load(String code) {

        return switch (code) {
            case "HIH" -> MovementReason.HELP_IN_HOUSE;
            case "RS" -> MovementReason.ROOM_STATE;
            case "CB" -> MovementReason.CHILD_BEHAVIOR;
            case "SH" -> MovementReason.SCHOOL_HOMEWORK;
            case "M" -> MovementReason.MEAL;
            default -> throw new MoneyMovementException("Le code: " + code + " n'existe pas");
        };
    }
}
