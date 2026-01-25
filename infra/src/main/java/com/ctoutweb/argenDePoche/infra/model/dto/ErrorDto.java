package com.ctoutweb.argenDePoche.infra.model.dto;

public record ErrorDto(String errorMessage) {
  /**
   * Factory
   */
  public static ErrorDto createErrorDto(String errorMessage) {
    return new ErrorDto(errorMessage);
  }
}
