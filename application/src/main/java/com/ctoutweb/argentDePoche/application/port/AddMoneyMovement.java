package com.ctoutweb.argentDePoche.application.port;

import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;

import java.math.BigDecimal;

/**
 * Contrat sur les données composant un nouveau mouvement d'argent *
 */
public interface AddMoneyMovement {
    BigDecimal getFluctuationPrice();
    String getMovementActionCode();
    String getMovementReasonCode();
    ParentIdentity getInitiatedByParent();


}
