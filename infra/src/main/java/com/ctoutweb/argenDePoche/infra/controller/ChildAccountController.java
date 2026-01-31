package com.ctoutweb.argenDePoche.infra.controller;

import com.ctoutweb.argenDePoche.infra.model.dto.ChildAccountResponseDto;
import com.ctoutweb.argenDePoche.infra.model.dto.CreateChildAccountResponseDto;
import com.ctoutweb.argenDePoche.infra.model.dto.CreateChildAccountRequestDto;
import com.ctoutweb.argenDePoche.infra.service.ChildAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
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

    @PutMapping(path = "/update-image")
    public Mono<ResponseEntity<Long>> updateChildImage(
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
                                .created(location)
                                .body(responseDto.childAccountId());
                      }
              );
    }
}
