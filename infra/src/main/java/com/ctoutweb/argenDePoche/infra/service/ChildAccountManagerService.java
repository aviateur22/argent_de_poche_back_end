package com.ctoutweb.argenDePoche.infra.service;

import com.ctoutweb.argenDePoche.infra.model.dto.ChildAccountResponseDto;
import com.ctoutweb.argenDePoche.infra.model.dto.CreateChildAccountResponseDto;
import com.ctoutweb.argenDePoche.infra.model.dto.CreateChildAccountRequestDto;
import reactor.core.publisher.Mono;

public interface ChildAccountManagerService {
    /**
     * Creation d'un compte d'argent de piche
     *
     * @param dto Les données nécessaire à la creation
     *
     * @return L'identifiant du compte créé
     */
    Mono<CreateChildAccountResponseDto> createChildAccount(CreateChildAccountRequestDto dto);

    /**
     * Récupération des données d'un compte d'argent de poche
     *
     * @param parentId L'identifiant du parent
     * @param childAccountId L'identitifiant du compte
     *
     * @return Les données du compte d'argent de poche
     */
    Mono<ChildAccountResponseDto> loadChildAccount(long parentId, long childAccountId);
}
