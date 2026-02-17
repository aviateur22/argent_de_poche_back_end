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

    // Configuration Cors pour la gestion Product
    CorsConfiguration productCorsConfig = new CorsConfiguration();
    productCorsConfig.setAllowCredentials(true);
    productCorsConfig.setAllowedOrigins(Arrays.asList(corsDomains.split(",")));
    productCorsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS"));
    productCorsConfig.setAllowedHeaders(Arrays.asList( "multipart/form-data", "Content-Type", "Authorization", "Post-Csrf-Token"));
    productCorsConfig.setExposedHeaders(List.of("Post-Csrf-Token"));
    source.registerCorsConfiguration(apiVersion+"/child-accounts/**", productCorsConfig);
    return source;
  }
}
