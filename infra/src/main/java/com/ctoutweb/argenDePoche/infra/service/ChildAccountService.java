package com.ctoutweb.argenDePoche.infra.service;

import com.ctoutweb.argenDePoche.infra.model.dto.*;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface ChildAccountService {
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

    /**
     * Mise à jour de l'image de l'enfant
     *
     * @param childImageFile L'image de l'enfant
     *
     * @return Le nom random de l'image
     */
    Mono<UpdatedChildImageResponseDto> updateChildImage(FilePart childImageFile, long parentId, long childAccountId);
}
