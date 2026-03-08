package com.ctoutweb.argenDePoche.infra.exception;

import com.ctoutweb.argenDePoche.infra.model.dto.ErrorDto;
import com.ctoutweb.argentDePoche.application.exception.*;
import com.ctoutweb.argentDePoche.core.domain.exception.ChildNameException;
import com.ctoutweb.argentDePoche.core.domain.exception.UnvalidMoneyAtPeriodStartException;
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

  @ExceptionHandler(CalendarPeriodNotFound.class)
  public Mono<ResponseEntity<ErrorDto>> calendarPeriodNotFound(CalendarPeriodNotFound exception) {
    return Mono.just(
            ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorDto.createErrorDto(exception.getMessage()))
    );
  }

  @ExceptionHandler(BalanceReasonException.class)
  public Mono<ResponseEntity<ErrorDto>> balanceReasonException(BalanceReasonException exception) {
    return Mono.just(
            ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorDto.createErrorDto(exception.getMessage()))
    );
  }

  @ExceptionHandler(ChildImageExtensionInvalidExtension.class)
  public Mono<ResponseEntity<ErrorDto>> imageExtensionException(ChildImageExtensionInvalidExtension exception) {
    return Mono.just(
            ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorDto.createErrorDto(exception.getMessage()))
    );
  }

  @ExceptionHandler(ChildImageNotFoundException.class)
  public Mono<ResponseEntity<ErrorDto>> childImageNotFoundException(ChildImageNotFoundException exception) {
    return Mono.just(
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorDto.createErrorDto(exception.getMessage()))
    );
  }

  @ExceptionHandler(ChildNameException.class)
  public Mono<ResponseEntity<ErrorDto>> childNameException(ChildNameException exception) {
    return Mono.just(
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorDto.createErrorDto(exception.getMessage()))
    );
  }

  @ExceptionHandler(AuthenticationException.class)
  public Mono<ResponseEntity<ErrorDto>> authenticationExceptionNotFoundException(AuthenticationException exception) {
    return Mono.just(
            ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorDto.createErrorDto(exception.getMessage()))
    );
  }

  @ExceptionHandler(UnvalidMoneyAtPeriodStartException.class)
  public Mono<ResponseEntity<ErrorDto>> unvalidMoneyAtPeriodStartException(UnvalidMoneyAtPeriodStartException exception) {
    return Mono.just(
            ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorDto.createErrorDto(exception.getMessage()))
    );
  }

  @ExceptionHandler(ChildAccountDesactivateException.class)
  public  Mono<ResponseEntity<ErrorDto>> childAccountDesactivateException(ChildAccountDesactivateException exception) {
    return Mono.just(
            ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorDto.createErrorDto(exception.getMessage()))
    );
  }

}
