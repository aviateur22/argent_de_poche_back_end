package com.ctoutweb.argenDePoche.infra.controller;

import com.ctoutweb.argenDePoche.infra.model.dto.controller.LoginDto;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.LoginResponseDto;
import com.ctoutweb.argenDePoche.infra.service.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("${api.version}/auth")
public class AuthController {
  private static final Logger LOGGER = LogManager.getLogger();

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
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
}
