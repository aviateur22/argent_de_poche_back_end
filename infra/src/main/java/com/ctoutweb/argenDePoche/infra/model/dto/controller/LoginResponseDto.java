package com.ctoutweb.argenDePoche.infra.model.dto.controller;

import java.time.LocalDateTime;
import java.util.List;

public record LoginResponseDto(
        String jwt,
        Long parentId,
        String parentName,
        List<String> roles,
        LocalDateTime jwtExpiredAt,
        String message) implements ResponseMessage {
  @Override
  public String getMessage() {
    return message;
  }
}
