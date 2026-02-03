package com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account;

import com.ctoutweb.argentDePoche.core.domain.exception.MovementActionTypeNotFoundException;

public enum MovementActionType {
    ADD_MONEY("+"),
    REMOVE_MONEY("-");

    private MovementActionType(String actionCode) {
        this.actionCode = actionCode;
    }

    private String actionCode;

    public String getActionCode() {
        return actionCode;
    }

    public static MovementActionType load(String code) {
        final var addMovementMoney = "+";
        final var removeMovementMoney = "-";

        return switch (code) {
            case addMovementMoney -> MovementActionType.ADD_MONEY;
            case removeMovementMoney -> MovementActionType.REMOVE_MONEY;
            default -> throw new MovementActionTypeNotFoundException("Le code: " + code + " n'existe pas");
        };
    }
}
