package com.ctoutweb.argenDePoche.infra.exception;

/**
 *  Exception propagée quand le token JWT n'est pas valide
 */
public class AuthenticationTokenInvalidException extends RuntimeException {
  public AuthenticationTokenInvalidException(String message) {
    super(message);
  }
}
