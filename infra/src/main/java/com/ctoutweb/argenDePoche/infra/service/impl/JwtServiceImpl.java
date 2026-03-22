package com.ctoutweb.argenDePoche.infra.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ctoutweb.argenDePoche.infra.config.authentication.UserPrincipal;
import com.ctoutweb.argenDePoche.infra.exception.AuthenticationTokenExpiredException;
import com.ctoutweb.argenDePoche.infra.exception.AuthenticationTokenInvalidException;
import com.ctoutweb.argenDePoche.infra.model.jwt.JwtGenerated;
import com.ctoutweb.argenDePoche.infra.model.jwt.JwtGeneratedImpl;
import com.ctoutweb.argenDePoche.infra.repository.JwtRepository;
import com.ctoutweb.argenDePoche.infra.repository.entity.JwtEntity;
import com.ctoutweb.argenDePoche.infra.repository.entity.ParentEntity;
import com.ctoutweb.argenDePoche.infra.service.JwtService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ctoutweb.argenDePoche.infra.constant.JwtConstant.CLAIM_AUTHORITIES;
import static com.ctoutweb.argenDePoche.infra.constant.JwtConstant.CLAIM_ID;

@Service
public class JwtServiceImpl implements JwtService {
    private static final Logger LOGGER = LogManager.getLogger();

    @Value("${jwt.validity.hour}")
    Long jwtValidity;
    @Value("${jwt.secret.key}")
    String jwtSecret;
    @Value("${jwt.issuer}")
    String jwtIssuer;
    @Value("${zone.id}")
    String zoneId;
    private final JwtRepository jwtRepository;

    public JwtServiceImpl(JwtRepository jwtRepository) {
        this.jwtRepository = jwtRepository;
    }

    @Override
    public JwtGenerated generate(UserPrincipal userPrincipal) {

        Instant expiredAt = Instant.now().plus(Duration.ofHours(jwtValidity));
        byte[] timeNow = ("time now" +" " + System.currentTimeMillis()).getBytes();
        String jwtId = UUID.nameUUIDFromBytes(timeNow).toString();

        List<String> authorities = userPrincipal.getAuthorities()
                .stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        String token = JWT.create()
                .withSubject("seller")
                .withJWTId(jwtId)
                .withIssuer(jwtIssuer)
                .withExpiresAt(expiredAt)
                .withClaim(CLAIM_ID, userPrincipal.getId())
                .withClaim(CLAIM_AUTHORITIES, authorities)
                .sign(Algorithm.HMAC256(jwtSecret));

        return new JwtGeneratedImpl(jwtId, token, LocalDateTime.ofInstant(expiredAt, ZoneId.of(zoneId)));
    }

    @Override
    public Mono<DecodedJWT> validateAndDecode(String token) {
        try {
            DecodedJWT decodedJWT = JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .build()
                    .verify(token);

            return Mono.just(decodedJWT);

        } catch (SignatureVerificationException exception) {
            LOGGER.error(exception.getMessage());
            throw new AuthenticationTokenInvalidException("Le token JWT n'est pas valide");
        } catch (TokenExpiredException exception) {
            LOGGER.error(exception.getMessage());
            throw new AuthenticationTokenExpiredException("Le token JWT est expiré");
        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
            throw new AuthenticationTokenInvalidException("Il y a eu une exception lors de la validation du token JWT");
        }
    }

    @Override
    public void deleteJwtBySellerId(long parentId) {
        ParentEntity parentLoginOut = new ParentEntity();
        parentLoginOut.setId(parentId);
        jwtRepository.deleteByParentId(parentLoginOut);
    }

    @Override
    public Mono<Void> saveJwt(Long parentId, JwtGenerated jwt, String email) {
        ParentEntity parentLogin = new ParentEntity();
        parentLogin.setId(parentId);

        JwtEntity insertJwtLogin = new JwtEntity();
        insertJwtLogin.setJwtToken(jwt.getJwtToken());
        insertJwtLogin.setJwtId(jwt.getJwtId());
        insertJwtLogin.setParentId(parentId);
        insertJwtLogin.setExpiredAt(jwt.getExpiredAt());
        insertJwtLogin.setEmail(email);
        insertJwtLogin.setIsValid(true);

        return jwtRepository.save(insertJwtLogin).then();
    }

    @Override
    public Mono<Boolean> isJwtUuidValid(String jwtUuid) {
        return jwtRepository
                .findFirstByJwtId(jwtUuid)
                .map(JwtEntity::getIsValid)
                .defaultIfEmpty(false);
    }
}
