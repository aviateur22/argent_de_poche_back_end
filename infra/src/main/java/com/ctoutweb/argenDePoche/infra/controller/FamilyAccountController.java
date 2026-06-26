package com.ctoutweb.argenDePoche.infra.controller;

import com.ctoutweb.argenDePoche.infra.model.dto.controller.CreateFamilyAccountRequestDto;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.CreateFamilyAccountResponseDto;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.familyAccountResponse.FamilyAccountResponseDto;
import com.ctoutweb.argenDePoche.infra.service.FamilyAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("${api.version}/family-accounts")
public class FamilyAccountController {
    private static final Logger LOGGER = LogManager.getLogger();

    @Value("${api.version}")
    String apiPath;

    private final FamilyAccountService familyAccountService;

    public FamilyAccountController(FamilyAccountService familyAccountService) {
        this.familyAccountService = familyAccountService;
    }

    @GetMapping("/parent/{parentId}/load-family-child-accounts")
    public Mono<ResponseEntity<FamilyAccountResponseDto>> loadChildAccount(@PathVariable Long parentId, Authentication authentication) {

        LOGGER.info(() -> "Création d'un nouveau compte familiale");

        LOGGER.debug(authentication.getPrincipal());
        return familyAccountService.loadFamily(parentId)
                .map(responseDto -> {
                            return ResponseEntity
                                    .ok()
                                    .body(responseDto);
                        }
                );
    }
}
