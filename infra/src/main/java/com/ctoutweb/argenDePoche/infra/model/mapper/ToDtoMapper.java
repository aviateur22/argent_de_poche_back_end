package com.ctoutweb.argenDePoche.infra.model.mapper;

import com.ctoutweb.argenDePoche.infra.model.dto.CreateChildAccountResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ToDtoMapper {

    /**
     * Map vers CreateChildAccountResponseDto
     *
     * @param createdChildAccountId L'identitifiant technique du compte d'argent de poche créé
     *
     * @return CreateChildAccountResponseDto
     */
    public CreateChildAccountResponseDto mapToCreateChildAccountResponseDto(long createdChildAccountId) {
        return new CreateChildAccountResponseDto(createdChildAccountId);
    }
}
