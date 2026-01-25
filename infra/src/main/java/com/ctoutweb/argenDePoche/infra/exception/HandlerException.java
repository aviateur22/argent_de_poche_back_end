package com.ctoutweb.argenDePoche.infra.exception;

import com.ctoutweb.argenDePoche.infra.model.dto.ErrorDto;
import com.ctoutweb.argentDePoche.application.exception.ChildAccountForbiddenException;
import com.ctoutweb.argentDePoche.application.exception.FamilyAccountForbiddenException;
import com.ctoutweb.argentDePoche.application.exception.FamilyConflictException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class HandlerException {
  private static final Logger LOGGER = LogManager.getLogger();

  @ExceptionHandler(EmailExistException.class)
  public Mono<ResponseEntity<ErrorDto>> emailException(EmailExistException exception) {
    return Mono.just(
            ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorDto.createErrorDto(exception.getMessage()))
    );
  }

  @ExceptionHandler(ChildAccountForbiddenException.class)
  public Mono<ResponseEntity<ErrorDto>> childMoneyForbiddenException(ChildAccountForbiddenException exception) {
    return Mono.just(
            ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorDto.createErrorDto(exception.getMessage()))
    );
  }

  @ExceptionHandler(FamilyAccountForbiddenException.class)
  public Mono<ResponseEntity<ErrorDto>> familyAccountForbiddenException(FamilyAccountForbiddenException exception) {
    return Mono.just(
            ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorDto.createErrorDto(exception.getMessage()))
    );
  }

  @ExceptionHandler(FamilyConflictException.class)
  public Mono<ResponseEntity<ErrorDto>> familyConflictException(FamilyConflictException exception) {
    return Mono.just(
            ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorDto.createErrorDto(exception.getMessage()))
    );
  }

}
