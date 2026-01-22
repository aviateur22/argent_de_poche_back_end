package com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account;

import com.ctoutweb.argentDePoche.core.domain.exception.MovementActionTypeException;

public enum MovementActionType {
    ADD_MONEY,
    REMOVE_MONEY;

    public static MovementActionType load(String code) {
        return switch (code) {
            case "AM" -> MovementActionType.ADD_MONEY;
            case "RM" -> MovementActionType.REMOVE_MONEY;
            default -> throw new MovementActionTypeException("Le code: " + code + " n'existe pas");
        };
    }
}
