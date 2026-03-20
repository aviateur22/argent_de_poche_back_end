package com.ctoutweb.argenDePoche.infra.adapter.primaryAdapter;

import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToCoreMapper;
import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToDtoMapper;
import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToInfraMapper;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.DisplayChildAccountInfoResponseDto;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.childAccount.ChildAccountResponseDto;
import com.ctoutweb.argenDePoche.infra.model.dto.UpdatedChildImageDto;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.UpdatedChildAccountResponseDto;
import com.ctoutweb.argentDePoche.application.api.ChildAccountUseCase;
import com.ctoutweb.argentDePoche.application.port.GenerateQrCode;
import com.ctoutweb.argentDePoche.application.query.dto.ChildAccountDto;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.childImage.ImageExtension;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;
import org.springframework.core.io.buffer.DataBuffer;
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
     * @param defaultImageName nom de l'image imposé par default à la création du compte
     * @param defaultImageExtension Extension de l'image par default
     *
     * @return L'identifiant du compte créé
     */
    public Mono<Long> createChildAccount(
            long parentCreatingChildAccount,
            String childName,
            String defaultImageName,
            String defaultImageExtension) {
        var parentIdentity = toCoreMapper.toParentIdentity(parentCreatingChildAccount);

        ImageExtension imageExtension = toCoreMapper.toImageExtension(defaultImageExtension);

        return childAccountUseCase.createChildMoneyAccount(
                parentIdentity,
                childName,
                defaultImageName,
                imageExtension
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
                .map(childAccount -> {
                    var childName= childAccount.childName();
                    var responseMessage = String.format("Données d'argent de poche de %s", childName);
                    return toDtoMapper.toChildAccountResponseDto(childAccount, responseMessage);
                });
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
     * @param imageExtension L'extension de l'image
     *
     * @return renvoie L'identifiant du compte, le nom de la nouvelle image, le nom de l'ancienne image
     */
    public Mono<UpdatedChildImageDto> updateChildImage(long parentId, long childAccountId, String imageExtension) {
        ImageExtension coreImageExtension = toCoreMapper.toImageExtension(imageExtension);
        var parentIdentity = toCoreMapper.toParentIdentity(parentId);
        var childAccountIdentity = toCoreMapper.toChildAccountIdentity(childAccountId);
        return childAccountUseCase.updateChildImage(childAccountIdentity, parentIdentity, coreImageExtension)
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
     * Réinitialisation de l'argent restant a son niveau initial
     *
     * @param parentId Lidentifiant du parent
     * @param childAccountId Le compte d'argent de poche touché
     *
     * @return  L'identifiant du compte d'argent de poche qui a été modifié
     */
    public Mono<UpdatedChildAccountResponseDto> reinitializeRemainingMoney(long parentId, long childAccountId) {
        var parentIdentity = toCoreMapper.toParentIdentity(parentId);
        var childAccountIdentity = toCoreMapper.toChildAccountIdentity(childAccountId);

        return childAccountUseCase.reinitializeRemainingMoney(childAccountIdentity, parentIdentity)
                .map(s-> toDtoMapper.toUpdatedChildResponseDto(s));
    }

    /**
     * Validation du stream d'une image
     *
     * @param parentId Lidentifiant du parent
     * @param childAccountId L'identifiant du compte d'argent de poche
     *
     * @return Renvoie l'extension de l'image a charger
     */
    public Mono<String> streamChildImage(long parentId, long childAccountId) {
        var parentIdentity = toCoreMapper.toParentIdentity(parentId);
        var childAccountIdentity = toCoreMapper.toChildAccountIdentity(childAccountId);

        return childAccountUseCase.streamChildImage(childAccountIdentity, parentIdentity)
                .map(ImageExtension::getFileExtensionText);
    }

    /**
     * Mise a jour du nom de l'enfant qui est associé au compte
     *
     * @param parentId L'identification du parent
     * @param childAccountId Le compte d'argent de poche impacté
     * @param updateChildName Le nouveau nom de l'enfant
     *
     * @return L'identifiant du compte modifié
     */
    public Mono<UpdatedChildAccountResponseDto> updateChildName(long parentId, long childAccountId, String updateChildName) {
        var parentIdentity = toCoreMapper.toParentIdentity(parentId);
        var childAccountIdentity = toCoreMapper.toChildAccountIdentity(childAccountId);

        return childAccountUseCase.updateChildName(childAccountIdentity, parentIdentity, updateChildName)
                .map(s-> toDtoMapper.toUpdatedChildResponseDto(s));

    }

    /**
     * Désactivation d'un compte
     *
     * @param parentId L'identification du parent désactivant le compte
     * @param childAccountId Le compte d'argent de poche désactivé
     *
     * @return L'identifiant du compte modifié
     */
    public Mono<UpdatedChildAccountResponseDto> desactivateChildAccount(long parentId, long childAccountId) {
        var parentIdentity = toCoreMapper.toParentIdentity(parentId);
        var childAccountIdentity = toCoreMapper.toChildAccountIdentity(childAccountId);

        return childAccountUseCase.desactivateChildAccount(childAccountIdentity, parentIdentity)
                .map(s-> toDtoMapper.toUpdatedChildResponseDto(s));
    }

    /**
     * Generation d'un QR code
     *
     * @param parentId L'identifiant du parent
     * @param childAccountId Le compte de l'enfant
     * @param urlToDisplayInQrCode L'url a afficher dans le QR code
     *
     * @return Un stream du QR code
     */
    public Mono<DataBuffer> generateQrCode(long parentId, long childAccountId, String urlToDisplayInQrCode) {
        var parentIdentity = toCoreMapper.toParentIdentity(parentId);
        var childAccountIdentity = toCoreMapper.toChildAccountIdentity(childAccountId);

        GenerateQrCode generateQrCode = new GenerateQrCode() {

            @Override
            public ChildMoneyAccountIdentity getChildMoneyAccountId() {
                return childAccountIdentity;
            }

            @Override
            public ParentIdentity getParentIdentity() {
                return parentIdentity;
            }

            @Override
            public String getUrlToDisplayInQrCode() {
                return urlToDisplayInQrCode;
            }
        };

        return childAccountUseCase.generateChildAccountQrCode(generateQrCode)
                .map(toDtoMapper::toDataBuffer);
    }

    public Mono<DisplayChildAccountInfoResponseDto> displayChildAccountInfo(long childAccountId) {
        var childAccountIdentity = toCoreMapper.toChildAccountIdentity(childAccountId);
        return childAccountUseCase.displayChildAccountInfo(childAccountIdentity)
                .map(toDtoMapper::toDisplayChildAccountInfoResponseDto);
    }
}
