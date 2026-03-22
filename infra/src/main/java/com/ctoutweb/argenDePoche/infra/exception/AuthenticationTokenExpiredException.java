package com.ctoutweb.argenDePoche.infra.exception;

/**
 * Exception propagée quand le token JWT est expirée
 */
public class AuthenticationTokenExpiredException extends RuntimeException {
  public AuthenticationTokenExpiredException(String message) {
    super(message);
  }
}
