package com.ctoutweb.argenDePoche.infra.service;

import com.ctoutweb.argenDePoche.infra.model.dto.ImageStreaming;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.*;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.childAccount.ChildAccountResponseDto;
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
     * @return L'identifiant du compte d'argent de poche
     */
    Mono<UpdatedChildAccountResponseDto> updateChildImage(FilePart childImageFile, long parentId, long childAccountId);

    /**
     * Mise a jour du prénom de l'enfant
     *
     * @param dto Les données permttant de mettre à jour le nom de l'enfant
     *
     * @return L'identifiant du compte d'argent de poche
     */
    Mono<UpdatedChildAccountResponseDto> updateChildName(UpdateChildNameRequestDto dto);

    /**
     * Mise à jour de l'argent de poche disponible en début de periode
     *
     * @param dto Les données de mise a jour
     *
     * @return L'identifiant du compte d'argent de poche
     */
    Mono<UpdatedChildAccountResponseDto> updateChildMoneyAtPeriodStart(UpdateChildMoneyAtPeriodStartRequestDto dto);

    /**
     * Ajout d'un nouveau movement d'argent sur le compte d'argent de poche
     *
     * @param dto Les données liée a ce movement d'argent
     *
     * @return L'identifiant du compte d'argent de poche mise à jour
     */
    Mono<UpdatedChildAccountResponseDto> addMoneyMovement(AddMoneyMovementRequestDto dto);

    /**
     * Reinitialisation de l'argent restant
     *
     * @param parentId L'identifiant du parent faisant la reinitialmisation
     * @param childAccountId Le compte d'argent de poche impacté
     *
     * @return L'identifiant du compte d'argent de poche mise à jour
     */
    Mono<UpdatedChildAccountResponseDto> reinitializeRemainingMoney(long parentId, long childAccountId);

    /**
     * Stream l'image d'un compte d'argent de poche
     *
     * @param parentId L'identifiant du parent
     * @param childAccountId L'identifiant de ompte d'argent de poche
     * @param childImageName Le nom de l'image a streamer
     *
     * @return Le stream + le Mime type du fichier a streamer
     */
    Mono<ImageStreaming> streamChildImage(long parentId, long childAccountId, String childImageName);

    /**
     * Désactivation du compte d'argent de poche d'un enfant
     *
     * @param parentId
     * @param childAccountId
     * @return Renvoie l'identifiant du compte qui a été desaxtivé
     */
    Mono<UpdatedChildAccountResponseDto> desactivateChildAccount(Long parentId, Long childAccountId);
}
