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
    productCorsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT"));
    productCorsConfig.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "Post-Csrf-Token"));
    productCorsConfig.setExposedHeaders(List.of("Post-Csrf-Token"));
    source.registerCorsConfiguration(apiVersion+"/products/**", productCorsConfig);
    //source.registerCorsConfiguration("/"+ applicationName+apiVersion+"/products/**", productCorsConfig);

    // Configuration Cors pour la gestion des charts
    CorsConfiguration chartsCorsConfig = new CorsConfiguration();
    chartsCorsConfig.setAllowCredentials(true);
    chartsCorsConfig.setAllowedOrigins(Arrays.asList(corsDomains.split(",")));
    chartsCorsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT"));
    chartsCorsConfig.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "Post-Csrf-Token"));
    productCorsConfig.setExposedHeaders(List.of("Post-Csrf-Token"));
    source.registerCorsConfiguration(apiVersion+"/charts/**", chartsCorsConfig);

  // Configuration Cors pour la gestion des auth
  CorsConfiguration authCorsConfig = new CorsConfiguration();
  authCorsConfig.setAllowCredentials(true);
  authCorsConfig.setAllowedOrigins(Arrays.asList(corsDomains.split(",")));
  authCorsConfig.setAllowedMethods(Arrays.asList("POST"));
  authCorsConfig.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "Post-Csrf-Token"));
  authCorsConfig.setExposedHeaders(List.of("Post-Csrf-Token"));
  source.registerCorsConfiguration(apiVersion+"/auth/**", authCorsConfig);
  return source;
  }
}
