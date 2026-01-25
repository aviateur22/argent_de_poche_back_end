package com.ctoutweb.argenDePoche.infra.service;

import com.ctoutweb.argenDePoche.infra.model.dto.ChildAccountResponseDto;
import com.ctoutweb.argenDePoche.infra.model.dto.CreateChildAccountResponseDto;
import com.ctoutweb.argenDePoche.infra.model.dto.CreateChildAccountRequestDto;
import reactor.core.publisher.Mono;

public interface ChildAccountManagerService {
    Mono<CreateChildAccountResponseDto> createChildAccount(CreateChildAccountRequestDto dto);

    Mono<ChildAccountResponseDto> loadChildAccount(long parentId, long childAccountId);
}
