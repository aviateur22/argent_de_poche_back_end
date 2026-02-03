package com.ctoutweb.argentDePoche.application.port;

import java.math.BigDecimal;

/**
 * Contrat sur les informations composant un nouveau mouvement d'argent *
 */
public interface AddMoneyMovementReason {
    BigDecimal getFluctuationPrice();
    String getMovementReasonCode();
    String getMovementReasonName();

}
