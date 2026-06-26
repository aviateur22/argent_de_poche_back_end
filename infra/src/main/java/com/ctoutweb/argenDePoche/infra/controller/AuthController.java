package com.ctoutweb.argenDePoche.infra.controller;

import com.ctoutweb.argenDePoche.infra.model.dto.controller.*;
import com.ctoutweb.argenDePoche.infra.service.AuthService;
import com.ctoutweb.argenDePoche.infra.service.ChildAccountService;
import com.ctoutweb.argenDePoche.infra.service.FamilyAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * Regroupement des services accessibles sans token JWT
 */
@RestController
@RequestMapping("${api.version}/auth")
public class AuthController {
  private static final Logger LOGGER = LogManager.getLogger();

  @Value("${api.version}")
  String apiPath;

  private final AuthService authService;
  private final ChildAccountService childAccountService;
  private final FamilyAccountService familyAccountService;

  public AuthController(
          AuthService authService,
          ChildAccountService childAccountService,
          FamilyAccountService familyAccountService) {
    this.authService = authService;
    this.childAccountService = childAccountService;
    this.familyAccountService = familyAccountService;
  }

  @PostMapping("/login-to-family-account")
  Mono<ResponseEntity<LoginResponseDto>> login(@RequestBody LoginDto loginDto) {

    LOGGER.info(() -> "Login de " + loginDto.email());
    return authService.login(loginDto)
            .map(responseDto ->
                    ResponseEntity
                            .ok()
                            .body(responseDto)

            );
  }

  @GetMapping(value = "/flash/child-account/{childAccountId}/info")
  public Mono<ResponseEntity<DisplayChildAccountInfoResponseDto>> displayChildAccountInfo(@PathVariable Long childAccountId) {
    return childAccountService.displayChildAccountInfo(childAccountId)
            .map(responseDto -> {
                      return ResponseEntity
                              .ok()
                              .body(responseDto);
                    }
            );
  }

  @PostMapping("/create-family-account")
  public Mono<ResponseEntity<CreateFamilyAccountResponseDto>> createFamilyAccount(@RequestBody CreateFamilyAccountRequestDto dto) {
    LOGGER.info(() -> String.format("Création d'un compte familliale %s", dto));

    return familyAccountService.createFamilyAccount(dto)
            .map(responseDto -> {
                      URI location = URI.create( apiPath + "/family-accounts/" + responseDto.createdFamilyAccountId());
                      return ResponseEntity
                              .created(location)
                              .body(responseDto);
                    }
            );
  }
}
