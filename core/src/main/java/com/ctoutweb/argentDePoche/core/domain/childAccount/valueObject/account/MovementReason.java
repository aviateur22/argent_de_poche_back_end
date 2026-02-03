package com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account;

import com.ctoutweb.argentDePoche.core.domain.exception.MoneyMovementException;

public enum MovementReason {
    HELP_IN_HOUSE("hih"),
    ROOM_STATE("rs"),
    CHILD_BEHAVIOR("cb"),
    SCHOOL_HOMEWORK("sh"),
    MEAL("m");

    private String reasonCode;

    private MovementReason(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getCode() {
        return this.reasonCode;
    }
    /**
     * Renvoie la raison du mouvement d'argent
     *
     * @param code recu permettant de renvoyer un MovementReason
     *
     * @return MovementReason
     */
    public static MovementReason load(String code) {

        return switch (code.toLowerCase()) {
            case "hih" -> MovementReason.HELP_IN_HOUSE;
            case "rs" -> MovementReason.ROOM_STATE;
            case "cb" -> MovementReason.CHILD_BEHAVIOR;
            case "sh" -> MovementReason.SCHOOL_HOMEWORK;
            case "m" -> MovementReason.MEAL;
            default -> throw new MoneyMovementException("Le code: " + code + " n'existe pas");
        };
    }
}
