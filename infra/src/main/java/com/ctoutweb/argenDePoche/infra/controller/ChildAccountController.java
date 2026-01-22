package com.ctoutweb.argenDePoche.infra.controller;

import com.ctoutweb.argenDePoche.infra.model.dto.CreateChildAccountResponseDto;
import com.ctoutweb.argenDePoche.infra.model.dto.CreateChildAccountRequestDto;
import com.ctoutweb.argenDePoche.infra.model.dto.LoadChildAccountRequestDto;
import com.ctoutweb.argenDePoche.infra.service.ChildAccountManagerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.reactive.TransactionalOperator.*;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("${api.version}/child-accounts")
public class ChildAccountController {
    private static final Logger LOGGER = LogManager.getLogger();

    private final ChildAccountManagerService childAccountService;

    public ChildAccountController(ChildAccountManagerService childAccountService) {
        this.childAccountService = childAccountService;
    }

    @PostMapping("/")
    public Mono<ResponseEntity<CreateChildAccountResponseDto>> createChildAccount(@RequestBody CreateChildAccountRequestDto dto) {
        LOGGER.info(() -> "kkkk");
        return childAccountService.createChildAccount(dto)
            .map(responseDto -> {
                URI location = URI.create("/api/v1/child-accounts/" + responseDto.createdChildAccountId());
                return ResponseEntity
                    .created(location)
                    .body(responseDto);
                }
            );
    }

    @GetMapping("/")
    public Mono<ResponseEntity<CreateChildAccountResponseDto>> loadChildAccount(LoadChildAccountRequestDto dto) {
        return childAccountService.loadChildAccount(dto)
                .map(ResponseEntity::ok);
    }
}
