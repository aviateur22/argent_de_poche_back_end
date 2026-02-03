package com.ctoutweb.argenDePoche.infra.adapter.primaryAdapter;

import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToCoreMapper;
import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToDtoMapper;
import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToInfraMapper;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.ChildAccountResponseDto;
import com.ctoutweb.argenDePoche.infra.model.dto.UpdatedChildImageDto;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.UpdatedChildAccountResponseDto;
import com.ctoutweb.argentDePoche.application.api.ChildAccountUseCase;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class ChildAccountUseCaseAdapter {
    private final ChildAccountUseCase childAccountUseCase;
    private final ToCoreMapper toCoreMapper;
    private final ToInfraMapper toInfraMapper;
    private final ToDtoMapper toDtoMapper;

    public ChildAccountUseCaseAdapter(
            ChildAccountUseCase childAccountManager,
            ToCoreMapper toCoreMapper,
            ToInfraMapper toInfraMapper,
            ToDtoMapper toDtoMapper) {
        this.childAccountUseCase = childAccountManager;
        this.toCoreMapper = toCoreMapper;
        this.toInfraMapper = toInfraMapper;
      this.toDtoMapper = toDtoMapper;
    }

    /**
     * Creation d'un nouveeau compte d'argent de poche
     *
     * @param parentCreatingChildAccount Lidentifiant du parent faisant la création
     * @param childName Le nom de l'enfant
     *
     * @return L'identifiant du compte créé
     */
    public Mono<Long> createChildAccount(long parentCreatingChildAccount, String childName) {
        var parentIdentity = toCoreMapper.toParentIdentity(parentCreatingChildAccount);

        return childAccountUseCase.createChildMoneyAccount(
            parentIdentity,
            childName
        ).map(toInfraMapper::toTechnicalId);
    }

    /**
     * Chargement des données d'un compte d'argent de poche
     *
     * @param parentId L'identifiant du parent faisant la demande
     * @param childAccountId Le compte d'argent de poche demandé
     *
     * @return Les données du compte d'argent de poche
     */
    public Mono<ChildAccountResponseDto> loadChildAccount(long parentId, long childAccountId) {
        var parentIdentity = toCoreMapper.toParentIdentity(parentId);
        var childAccountIdentity = toCoreMapper.toChildAccountIdentity(childAccountId);
        return childAccountUseCase.loadChildAccount(childAccountIdentity, parentIdentity)
                .map(toDtoMapper::toChildAccountResponseDto);
    }

    /**
     * Initialisation des données pour une nouvelle période
     * Methode croné tous les dimanche minuit
     *
     * @return Boolean
     */
    public Mono<Boolean> initializeNextPeriod() {
        return childAccountUseCase.initializeNextCalendarPeriod();
    }


    /**
     * Mise à jour de l'image de l'enfant
     *
     * @param parentId L'identifiant du parent faisant l'action
     * @param childAccountId Le compte de l'enfant qui est modifié
     *
     * @return renvoie L'identifiant du compte, le nom de la nouvelle image, le nom de l'ancienne image
     */
    public Mono<UpdatedChildImageDto> updateChildImage(long parentId, long childAccountId) {
        var parentIdentity = toCoreMapper.toParentIdentity(parentId);
        var childAccountIdentity = toCoreMapper.toChildAccountIdentity(childAccountId);
        return childAccountUseCase.updateChildImage(childAccountIdentity, parentIdentity)
                .map(toDtoMapper::toUpdatedChildImageDto);
    }

    /**
     * Mise a jour de l'argent de poche disponible en debut de période
     *
     * @param parentId Le parent faisont l'action
     * @param childAccountId Le compte d'argent de poche
     * @param moneyAtPeriodStart Le nouvel argent de poche
     *
     * @return L'identifiant du compte d'argent de poche disponible
     */
    public Mono<UpdatedChildAccountResponseDto> updateChildMoneyAtPeriodStart(long parentId, long childAccountId, BigDecimal moneyAtPeriodStart) {
        var parentIdentity = toCoreMapper.toParentIdentity(parentId);
        var childAccountIdentity = toCoreMapper.toChildAccountIdentity(childAccountId);

        return childAccountUseCase.modulateInitialChildMoney(childAccountIdentity, parentIdentity, moneyAtPeriodStart)
                .map(s-> toDtoMapper.toUpdatedChildResponseDto(s));

    }

    /**
     * Ajout d'un mouvement d'argent sur un compte d'argent de poche
     *
     * @param parentId L'identification du parent faisant l'action
     * @param childAccountId Lidentitifaction du compte d'argent depoche
     * @param movementReasonCode Le code de la rasion du mouvement d'argent
     * @param movementActionCode L'action d'ajout ou de retrait d'argent
     *
     * @return L'identifiant du compte d'argent de poche qui a été modifié
     */
    public Mono<UpdatedChildAccountResponseDto> addMoneyMovement(long parentId, long childAccountId, String movementReasonCode, String movementActionCode) {
        var parentIdentity = toCoreMapper.toParentIdentity(parentId);
        var childAccountIdentity = toCoreMapper.toChildAccountIdentity(childAccountId);

        return childAccountUseCase.addChildMoneyMovement(childAccountIdentity, parentIdentity, movementReasonCode, movementActionCode)
                .map(s-> toDtoMapper.toUpdatedChildResponseDto(s));
    }

    /**
     *
     * @param parentId
     * @param childAccountId
     * @return
     */
    public Mono<UpdatedChildAccountResponseDto> reinitializeRemainingMoney(long parentId, long childAccountId) {
        var parentIdentity = toCoreMapper.toParentIdentity(parentId);
        var childAccountIdentity = toCoreMapper.toChildAccountIdentity(childAccountId);

        return childAccountUseCase.reinitializeRemainingMoney(childAccountIdentity, parentIdentity)
                .map(s-> toDtoMapper.toUpdatedChildResponseDto(s));
    }



}
