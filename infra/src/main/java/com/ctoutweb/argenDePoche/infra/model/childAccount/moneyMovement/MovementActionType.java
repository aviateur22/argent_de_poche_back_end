package com.ctoutweb.argenDePoche.infra.model.childAccount.moneyMovement;

import com.ctoutweb.argenDePoche.infra.exception.BalanceTypeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum MovementActionType {
    ADD("+"),
    REMOVE("-");

    private static final Logger LOGGER = LogManager.getLogger();

    final String balanceSign;

    private MovementActionType(String balanceSign) {
        this.balanceSign = balanceSign;
    }

    public static MovementActionType findBalanceActionType(String balanceSign) throws BalanceTypeException {
        LOGGER.info(() -> String.format("Signe de la nouvelle balance monétaire de l'argent de poche à ajouter: %s ", balanceSign));

        for(MovementActionType balanceActionType: MovementActionType.values()){
            if(balanceActionType.balanceSign.equalsIgnoreCase(balanceSign))
                return balanceActionType;
        }
        throw new BalanceTypeException("Le type de balance afin de moduler l'argent de poche n'est pas valide");
    }


}
