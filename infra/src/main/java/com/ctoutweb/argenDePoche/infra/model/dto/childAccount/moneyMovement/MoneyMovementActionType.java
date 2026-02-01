package com.ctoutweb.argenDePoche.infra.model.dto.childAccount.moneyMovement;

import com.ctoutweb.argenDePoche.infra.exception.BalanceTypeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum MoneyMovementActionType {
    ADD("+"),
    REMOVE("-");

    private static final Logger LOGGER = LogManager.getLogger();

    final String actionCode;

    private MoneyMovementActionType(String balanceSign) {
        this.actionCode = balanceSign;
    }

    public String actionCode() {
        return this.actionCode;
    }

    public static MoneyMovementActionType findBalanceActionType(String balanceSign) throws BalanceTypeException {
        LOGGER.info(() -> String.format("Signe de la nouvelle balance monétaire de l'argent de poche à ajouter: %s ", balanceSign));

        for(MoneyMovementActionType balanceActionType: MoneyMovementActionType.values()){
            if(balanceActionType.actionCode.equalsIgnoreCase(balanceSign))
                return balanceActionType;
        }
        throw new BalanceTypeException("Le type de balance afin de moduler l'argent de poche n'est pas valide");
    }


}
