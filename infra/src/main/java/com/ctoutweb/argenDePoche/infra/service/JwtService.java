package com.ctoutweb.argenDePoche.infra.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ctoutweb.argenDePoche.infra.config.authentication.UserPrincipal;
import com.ctoutweb.argenDePoche.infra.model.jwt.JwtGenerated;
import reactor.core.publisher.Mono;

import java.util.Optional;


public interface JwtService {

  /**
   * Génération d'un JWT incluant les données UserPrincipal
   *
   * @param userPrincipal UserPrincipal - Personne authentifié générant un Jwt
   *
   * @return Le jwt généré
   */
  public JwtGenerated generate(UserPrincipal userPrincipal);

  /**
   * Decoded un JWT
   *
   * @param token String - Jwt à verifier
   *
   * @return Le jwt decodé
   */
  public Optional<DecodedJWT> validateAndDecode(String token);

  /**
   * Suppression d'un JWT a partir d'un email
   *
   * @param SellerId L'identifiant du vendeur
   */
  public void deleteJwtBySellerId(long SellerId);

  /**
   * Sauvegarde d'un JWT
   *
   * @param userId Long Id de la personne qui se connecte
   * @param jwt IJwtIssue JWT issue
   * @param email String Email de la personne
   */
  public Mono<Void> saveJwt(Long userId, JwtGenerated jwt, String email);

  /**
   * Vérification si un JWT est bien présent et valide en base pour poursuivre une requete
   *
   * @param jwtUuid L'identifiant du JWT
   *
   * @return Treu si le JWT est en base et valide
   */
  public Mono<Boolean> isJwtUuidValid(String jwtUuid);

}
