package com.ctoutweb.argenDePoche.infra.config;

import com.ctoutweb.argenDePoche.infra.config.authentication.CustomAuthenicationProvider;
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

  @Bean
  SecurityWebFilterChain securityFilterChain(
          @Qualifier("corsConfiguration") CorsConfigurationSource corsConfigurationSource,
          ServerHttpSecurity http,
          CustomAuthenicationProvider customAuthenicationProvider) throws Exception {
    LOGGER.debug(apiVersion);
    http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .cors(cors->cors.configurationSource(corsConfigurationSource))
            .authenticationManager(customAuthenicationProvider)
            .authorizeExchange(exchange -> exchange
                    .pathMatchers(
                            apiVersion+"/auth/**",
                            apiVersion+"/child-accounts/**",
                            apiVersion+"/family-accounts/**",
                            apiVersion+"/admin/**")
                    .permitAll()
                    .anyExchange().authenticated());

    return http.build();
  }
}
