package com.ctoutweb.argenDePoche.infra.model.jwt;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public record JwtGeneratedImpl(String jwtId, String jwtToken, LocalDateTime expiredAt) implements JwtGenerated {
    @Override
    public String getJwtId() {
        return jwtId;
    }

    @Override
    public String getJwtToken() {
        return jwtToken;
    }

    @Override
    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }
}
