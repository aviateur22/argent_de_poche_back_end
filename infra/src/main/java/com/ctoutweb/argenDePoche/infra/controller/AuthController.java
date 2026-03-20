package com.ctoutweb.argenDePoche.infra.controller;

import com.ctoutweb.argenDePoche.infra.model.dto.controller.DisplayChildAccountInfoResponseDto;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.LoginDto;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.LoginResponseDto;
import com.ctoutweb.argenDePoche.infra.service.AuthService;
import com.ctoutweb.argenDePoche.infra.service.ChildAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("${api.version}/auth")
public class AuthController {
  private static final Logger LOGGER = LogManager.getLogger();

  private final AuthService authService;
  private final ChildAccountService childAccountService;

  public AuthController(AuthService authService, ChildAccountService childAccountService) {
    this.authService = authService;
    this.childAccountService = childAccountService;
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

  @GetMapping(value = "child-accounts/display/child-account/{childAccountId}/info")
  public Mono<ResponseEntity<DisplayChildAccountInfoResponseDto>> displayChildAccountInfo(@PathVariable Long childAccountId) {
    return childAccountService.displayChildAccountInfo(childAccountId)
            .map(responseDto -> {
                      return ResponseEntity
                              .ok()
                              .body(responseDto);
                    }
            );
  }
}
