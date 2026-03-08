package com.ctoutweb.argenDePoche.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;


import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

 // Domaine ouvert à l'API
  @Value("${cors.domains}")
  String corsDomains;

  @Value("${api.version}")
  String apiVersion;

  @Bean(name = "corsConfiguration")
  public CorsConfigurationSource corsConfigurationSource() {
    UrlBasedCorsConfigurationSource source =new UrlBasedCorsConfigurationSource();

    // Configuration Cors pour la gestion compte enfant
    CorsConfiguration childAccount = new CorsConfiguration();
    childAccount.setAllowCredentials(true);
    childAccount.setAllowedOrigins(Arrays.asList(corsDomains.split(",")));
    childAccount.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    childAccount.setAllowedHeaders(Arrays.asList( "multipart/form-data", "Content-Type", "Authorization", "Post-Csrf-Token"));
    childAccount.setExposedHeaders(List.of("Post-Csrf-Token"));
    source.registerCorsConfiguration(apiVersion+"/child-accounts/**", childAccount);

    // Configuration Cors pour la gestion famille
    CorsConfiguration familyAccount = new CorsConfiguration();
    familyAccount.setAllowCredentials(true);
    familyAccount.setAllowedOrigins(Arrays.asList(corsDomains.split(",")));
    familyAccount.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS"));
    familyAccount.setAllowedHeaders(Arrays.asList( "multipart/form-data", "Content-Type", "Authorization", "Post-Csrf-Token"));
    familyAccount.setExposedHeaders(List.of("Post-Csrf-Token"));
    source.registerCorsConfiguration(apiVersion+"/family-accounts/**", familyAccount);

    // Configuration Cors pour la gestion auth
    CorsConfiguration auth = new CorsConfiguration();
    auth.setAllowCredentials(true);
    auth.setAllowedOrigins(Arrays.asList(corsDomains.split(",")));
    auth.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS"));
    auth.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "Post-Csrf-Token"));
    auth.setExposedHeaders(List.of("Post-Csrf-Token"));
    source.registerCorsConfiguration(apiVersion+"/auth/**", auth);

    return source;
  }
}
