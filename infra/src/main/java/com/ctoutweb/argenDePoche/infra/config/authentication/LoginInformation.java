package com.ctoutweb.argenDePoche.infra.config.authentication;

import com.ctoutweb.argenDePoche.infra.util.DateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class LoginInformation {

    @Value("${zone.id}")
    String zoneId;

    /**
     * Vérification si un process de login peut avoir lieu
     *
     * @param actualLoginTime L'heure de connsion du parent
     * @param delayLoginUntil L'heure d'autorisation de la prochaine connexion
     *
     * @return True si le login peut avoir lieu
     */
    public boolean isLoginEnable(LocalDateTime actualLoginTime, LocalDateTime delayLoginUntil) {
        return actualLoginTime.isAfter(delayLoginUntil);
    }

    /**
     * Renvoie l'heure initialement en UTC avec l'integration de la zoneId
     *
     * @param utcDelayLoginUntil L'heure d'autorisation de la prochaine connexion
     *
     * @return L'heure de connexion avec la bonne zoneid
     */
    public ZonedDateTime getLoginTimeWithIntegratedZoneId(ZonedDateTime utcDelayLoginUntil) {
        return DateUtil.uctToZonedDateTime(ZoneId.of(zoneId), utcDelayLoginUntil);
    }
}
