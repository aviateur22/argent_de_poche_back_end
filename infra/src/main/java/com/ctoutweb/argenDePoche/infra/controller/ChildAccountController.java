package com.ctoutweb.argenDePoche.infra.controller;

import com.ctoutweb.argenDePoche.infra.model.dto.ChildAccountResponseDto;
import com.ctoutweb.argenDePoche.infra.model.dto.CreateChildAccountResponseDto;
import com.ctoutweb.argenDePoche.infra.model.dto.CreateChildAccountRequestDto;
import com.ctoutweb.argenDePoche.infra.service.ChildAccountManagerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("${api.version}/child-accounts")
public class ChildAccountController {
    private static final Logger LOGGER = LogManager.getLogger();

    @Value("${api.version}")
    String apiPath;
    private final ChildAccountManagerService childAccountService;

    public ChildAccountController(ChildAccountManagerService childAccountService) {
        this.childAccountService = childAccountService;
    }

    @PostMapping("/")
    public Mono<ResponseEntity<CreateChildAccountResponseDto>> createChildAccount(@RequestBody CreateChildAccountRequestDto dto) {
        LOGGER.info(() -> "Création d'un nouveau compte familiale");
        return childAccountService.createChildAccount(dto)
            .map(responseDto -> {
                URI location = URI.create(apiPath + "/child-accounts/" + responseDto.createdChildAccountId());
                return ResponseEntity
                    .created(location)
                    .body(responseDto);
                }
            );
    }

    @GetMapping("/")
    public Mono<ResponseEntity<ChildAccountResponseDto>> loadChildAccount(@RequestParam Long childAccountId, @RequestParam Long parenId) {
        LOGGER.info(() -> "Création d'un nouveau compte familiale");
        return childAccountService.loadChildAccount(parenId, childAccountId)
                .map(responseDto -> {
                    return ResponseEntity
                      .ok()
                      .body(responseDto);
                    }
                );
    }
}
