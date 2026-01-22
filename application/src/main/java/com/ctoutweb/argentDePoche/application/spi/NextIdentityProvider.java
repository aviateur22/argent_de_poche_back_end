package com.ctoutweb.argentDePoche.application.spi;

import com.ctoutweb.argentDePoche.application.port.NextChildAccountIdentities;
import com.ctoutweb.argentDePoche.application.port.NextFamilyAccountIdentities;

public interface NextIdentityProvider {

    /**
     * Génération identifiant compte pour enfant
     */
    NextChildAccountIdentities generateNextChildAccountIdentities();

    /**
     * Génération identitiiant compte de famille
     */
    NextFamilyAccountIdentities generateNextFamilyAccountIdentities();
}
