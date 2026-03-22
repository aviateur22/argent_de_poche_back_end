package com.ctoutweb.argenDePoche.infra.config.authorizationFilter;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class AuthorizationFilter implements WebFilter {
  private final AuthorizationManager authorizationManager;

  public AuthorizationFilter(AuthorizationManager authorizationManager) {
    this.authorizationManager = authorizationManager;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

    if(AuthorizedUri.isPathAuthorized(exchange.getRequest().getPath().value())) {
      return chain.filter(exchange);
    }

    return authorizationManager.getAuthentication(exchange)
        .flatMap(authentication ->
                chain.filter(exchange)
                        .contextWrite(
                                ReactiveSecurityContextHolder.withAuthentication(authentication)
                        )
        );

  }
}
