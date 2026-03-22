package com.ctoutweb.argenDePoche.infra.config.authorizationFilter;

import com.ctoutweb.argenDePoche.infra.config.authentication.UserPrincipal;
import com.ctoutweb.argenDePoche.infra.config.authentication.UserPrincipalAuthenticationToken;
import com.ctoutweb.argenDePoche.infra.exception.AuthenticationTokenInvalidException;
import com.ctoutweb.argenDePoche.infra.repository.ParentRepository;
import com.ctoutweb.argenDePoche.infra.repository.RoleParentRepository;
import com.ctoutweb.argenDePoche.infra.repository.RoleRepository;
import com.ctoutweb.argenDePoche.infra.repository.dto.ParentRoleProjection;
import com.ctoutweb.argenDePoche.infra.service.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.ctoutweb.argenDePoche.infra.constant.JwtConstant.CLAIM_ID;

@Component
public record AuthorizationManager(
        RoleParentRepository roleParentRepository,
        ParentRepository parentRepository,
        RoleRepository roleRepository,
        JwtService jwtService
) {
  /**
   * Gestion des authorization de la requete
   */

  public Mono<Authentication> getAuthentication(ServerWebExchange exchange) {
    String bearer = extractBearerFromRequest(exchange);

    return getParentFromBearer(bearer)
        .map(UserPrincipalAuthenticationToken::new);

  }

  /**
   * Extraction du bearer token de la requête
   *
   * @return Le bearer token
   *
   * @throws AuthenticationTokenInvalidException Erreur sur le bearer token
   */
  private String extractBearerFromRequest(ServerWebExchange exchange) throws AuthenticationTokenInvalidException {
    var token = exchange.getRequest().getHeaders().getFirst("authorization");

    if(!StringUtils.hasText(token) || !token.startsWith("Bearer "))
      throw new AuthenticationTokenInvalidException("Il n'y a pas de token JWT de disponible");

    return token.substring(7);
  }

  /**
   * Récupération du seller à partir du bearer token
   *
   * @param bearer Le token d'authorization extrait des headers
   *
   * @return Une nouvelle instance de l'utilisateur consommant le webservice.
   * Cette instance est construite à partir JWT
   */
  private Mono<UserPrincipal> getParentFromBearer(String bearer) {
    return jwtService.validateAndDecode(bearer)           // Validation du Token JWT
            .flatMap(decodedJWT -> {
              long parentId = decodedJWT.getClaim(CLAIM_ID).asLong();
              String jwtId = decodedJWT.getId();

              return jwtService.isJwtUuidValid(jwtId) // Vérification que le token JWT est enregistrée en base
                      .filter(isValid -> isValid)
                      .switchIfEmpty(Mono.error(new AuthenticationTokenInvalidException("Le token JWT n'est pas référencé en base")))
                      .flatMap(valid -> parentRepository.findById(parentId)
                              .switchIfEmpty(Mono.error(new AuthenticationTokenInvalidException("L'identifiant de l'utilisateur n'est pas référencé en base")))
                              .flatMap(parent ->
                                      roleParentRepository.getParentRoles(parentId)
                                              .collectList()
                                              .map(roles ->
                                                      roles
                                                              .stream()
                                                              .map(ParentRoleProjection::roleName)
                                                              .map(SimpleGrantedAuthority::new)
                                                              .toList()
                                              )
                                              .map(grantedAuthorities ->
                                                      UserPrincipal.initialize(parentId, parent.getEmail(), grantedAuthorities))
                              )
                      );
            });

  }
}
