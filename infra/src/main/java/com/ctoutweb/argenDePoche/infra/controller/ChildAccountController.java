package com.ctoutweb.argenDePoche.infra.controller;

import com.ctoutweb.argenDePoche.infra.model.dto.controller.*;
import com.ctoutweb.argenDePoche.infra.service.ChildAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("${api.version}/child-accounts")
public class ChildAccountController {
    private static final Logger LOGGER = LogManager.getLogger();

    @Value("${api.version}")
    String apiPath;

    private final ChildAccountService childAccountService;

    public ChildAccountController(@Qualifier("infraChildAccountServiceImpl") ChildAccountService childAccountService) {
        this.childAccountService = childAccountService;
    }

    @PostMapping("/")
    public Mono<ResponseEntity<CreateChildAccountResponseDto>> createChildAccount(@RequestBody CreateChildAccountRequestDto dto) {
        LOGGER.info(() -> "Création d'un nouveau compte pour enfant");
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
        LOGGER.info(() -> "Chargement d'un compte d'argent de poche");
        return childAccountService.loadChildAccount(parenId, childAccountId)
                .map(responseDto -> {
                    return ResponseEntity
                      .ok()
                      .body(responseDto);
                    }
                );
    }

    @PutMapping(path = "/update-image")
    public Mono<ResponseEntity<UpdatedChildAccountResponseDto>> updateChildImage(
            @RequestPart("image") FilePart childImage,
            @RequestPart("parentId") String parentIdStringFormated,
            @RequestPart("childAccountId") String childAccountIdStringFormated) {
      LOGGER.info(() -> "Mise à jour de l'image de l'enfant");
      long parentId = Long.parseLong(parentIdStringFormated);
      long childAccountId = Long.parseLong(childAccountIdStringFormated);

      return childAccountService.updateChildImage(childImage, parentId, childAccountId)
              .map(responseDto -> {
                        URI location = URI.create(apiPath + "/child-accounts/" + responseDto.childAccountId());
                        return ResponseEntity
                                .ok()
                                .location(location)
                                .body(responseDto);
                      }
              );
    }

    @PutMapping(path = "/update-initial-child-Money")
    public Mono<ResponseEntity<UpdatedChildAccountResponseDto>> updateMoneyAtPeriodStart(@RequestBody UpdateChildMoneyAtPeriodStartRequestDto dto) {
      LOGGER.info(() -> "Mise à jour de l'argent de poche disponible en début de periode");
      return childAccountService.updateChildMoneyAtPeriodStart(dto)
              .map(responseDto -> {
                URI location = URI.create(apiPath + "/child-accounts/" + responseDto.childAccountId());
                        return ResponseEntity
                                .ok()
                                .location(location)
                                .body(responseDto);
                      }
              );
    }

    @PostMapping(path = "/add-money-movment")
    public Mono<ResponseEntity<UpdatedChildAccountResponseDto>> addMoneyMovment(@RequestBody AddMoneyMovementRequestDto dto) {
      LOGGER.info(() -> "Ajout d'un mouvement d'argent");
      return childAccountService.addMoneyMovement(dto)
              .map(responseDto -> {
                        URI location = URI.create(apiPath + "/child-accounts/" + responseDto.childAccountId());
                        return ResponseEntity
                                .ok()
                                .location(location)
                                .body(responseDto);
                      }
              );
    }

  @PostMapping(path = "/reinitialize-remaining-money")
  public Mono<ResponseEntity<UpdatedChildAccountResponseDto>> reinitializeRemainingMoney(@RequestBody ReinitializeRemainingMoneyDto dto) {
    LOGGER.info(() -> "Reinitimaisation de l'argent de poche restant");
    return childAccountService.reinitializeRemainingMoney(dto.parentId(), dto.childAccountId())
            .map(responseDto -> {
                      URI location = URI.create(apiPath + "/child-accounts/" + responseDto.childAccountId());
                      return ResponseEntity
                              .ok()
                              .location(location)
                              .body(responseDto);
                    }
            );
  }

  @GetMapping(value = "/stream/image/{imageName}/parent/{parentId}/child-account/{childAccountId}")
  public Mono<ResponseEntity<Flux<DataBuffer>>> streamImage(@PathVariable String imageName, @PathVariable Long parentId, @PathVariable Long childAccountId) {

    return childAccountService.streamChildImage(parentId, childAccountId, imageName)
            .map(result -> ResponseEntity.ok()
            .contentType(result.imageMediaType())
            .body(result.childImageDate()));
  }

}
