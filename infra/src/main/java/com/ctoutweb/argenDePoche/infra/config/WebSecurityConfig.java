package com.ctoutweb.argenDePoche.infra.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.CorsConfigurationSource;


@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {
  private static final Logger LOGGER = LogManager.getLogger();

  @Value("${api.version}")
  private String apiVersion;
  private final CorsConfigurationSource corsConfigurationSource;


  public WebSecurityConfig(
          @Qualifier("corsConfiguration") CorsConfigurationSource corsConfigurationSource) {
      this.corsConfigurationSource = corsConfigurationSource;
  }

  @Bean
  SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
    LOGGER.debug(apiVersion);
    http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .cors(cors->cors.configurationSource(corsConfigurationSource))
            .authorizeExchange(exchange -> exchange
                    .pathMatchers(
                            apiVersion+"/child-accounts/**",
                            apiVersion+"/api")
                    .permitAll()
                    .anyExchange().authenticated());

    return http.build();
  }
}
