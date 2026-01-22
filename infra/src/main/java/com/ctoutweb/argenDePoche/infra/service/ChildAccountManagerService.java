package com.ctoutweb.argenDePoche.infra.service;

import com.ctoutweb.argenDePoche.infra.model.dto.CreateChildAccountResponseDto;
import com.ctoutweb.argenDePoche.infra.model.dto.CreateChildAccountRequestDto;
import com.ctoutweb.argenDePoche.infra.model.dto.LoadChildAccountRequestDto;
import reactor.core.publisher.Mono;

public interface ChildAccountManagerService {
    Mono<CreateChildAccountResponseDto> createChildAccount(CreateChildAccountRequestDto dto);

    Mono<CreateChildAccountResponseDto> loadChildAccount(LoadChildAccountRequestDto dto);
}
