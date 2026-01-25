package com.ctoutweb.argenDePoche.infra.exception;

public class EmailExistException extends RuntimeException {
  public EmailExistException(String message) {
    super(message);
  }
}
