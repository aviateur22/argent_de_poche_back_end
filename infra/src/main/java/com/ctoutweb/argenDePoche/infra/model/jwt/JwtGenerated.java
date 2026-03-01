package com.ctoutweb.argenDePoche.infra.model.jwt;

import java.time.LocalDateTime;

/**
 * Jwt généré
 */
public interface JwtGenerated {
    /**
     * Identitifaction du jwt
     *
     * @return Le UUID du JWT
     */
    String getJwtId();

    /**
     * Le token du jwt
     *
     * @return Le token du JWT
     */
    String getJwtToken();

    /**
     * Date d'expiration du JWT
     *
     * @return La date d'expiration du JWT
     */
    LocalDateTime getExpiredAt();
}
