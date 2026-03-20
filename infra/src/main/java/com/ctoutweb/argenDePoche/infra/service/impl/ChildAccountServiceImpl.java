package com.ctoutweb.argenDePoche.infra.service.impl;

import com.ctoutweb.argenDePoche.infra.adapter.primaryAdapter.ChildAccountUseCaseAdapter;
import com.ctoutweb.argenDePoche.infra.model.dto.ImageStreaming;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.*;
import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToDtoMapper;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.childAccount.ChildAccountResponseDto;
import com.ctoutweb.argenDePoche.infra.model.mapper.InfraMapper;
import com.ctoutweb.argenDePoche.infra.service.ChildAccountService;
import com.ctoutweb.argenDePoche.infra.service.ImageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.ctoutweb.argenDePoche.infra.util.FileUtil.getFileExtension;

@Service("infraChildAccountServiceImpl")
public class ChildAccountServiceImpl implements ChildAccountService {
    private static final Logger LOGGER = LogManager.getLogger();

    @Value("${default.child.image.name}")
    private String defaultImageName;

    @Value("${default.image.extension}")
    private String defaultImageExtension;

    @Value("${front.end.base.url}")
    private String frontEndBaseUrl;

    @Value("${qr.code.child.account.url}")
    private String qrCodeChildAccountUrl;

    private final TransactionalOperator txOperator;
    private final ToDtoMapper toDtoMapper;
    private final InfraMapper infraMapper;

    private final ChildAccountUseCaseAdapter childAccountUseCaseAdapter;
    private final ImageService imageService;

    public ChildAccountServiceImpl(
            TransactionalOperator txOperator,
            ToDtoMapper toDtoMapper, InfraMapper infraMapper,
            ChildAccountUseCaseAdapter childAccountManager, ImageService imageService) {
        this.txOperator = txOperator;
        this.toDtoMapper = toDtoMapper;
      this.infraMapper = infraMapper;
      this.childAccountUseCaseAdapter = childAccountManager;
      this.imageService = imageService;
    }

    @Override
    public Mono<CreateChildAccountResponseDto> createChildAccount(CreateChildAccountRequestDto dto) {
        return txOperator.transactional(
            Mono.defer(() ->
                childAccountUseCaseAdapter.createChildAccount(dto.parentId(), dto.childName(), defaultImageName, defaultImageExtension)
                        .map(toDtoMapper::mapToCreateChildAccountResponseDto)
                        .doOnSuccess(childAccount -> LOGGER.info(() -> String.format("Réussite de la création du compte d'argent de poche avec comme identifiant %s", childAccount.createdChildAccountId())))
                        .doOnError(e -> LOGGER.error("Erreur dans l'appel au service loadChildAccount", e))
            )
        );
    }

    @Override
    public Mono<ChildAccountResponseDto> loadChildAccount(long parentId, long childAccountId) {
        return txOperator.transactional(childAccountUseCaseAdapter.loadChildAccount(parentId, childAccountId)
                .doOnSuccess(childAccount -> LOGGER.info(() -> String.format("Réussite de la récupération des données du compte enfant %s", childAccount.childAccountIdentity())))
                .doOnError(e -> LOGGER.error("Erreur dans l'appel au service loadChildAccount", e)));
    }

    @Override
    public Mono<UpdatedChildAccountResponseDto> updateChildImage(FilePart childImageFile, long parentId, long childAccountId) {
        String imageExtension = getFileExtension(childImageFile);
        return txOperator.transactional(childAccountUseCaseAdapter.updateChildImage(parentId, childAccountId, imageExtension)
                        .flatMap(dto ->
                                imageService.saveImage(childImageFile, dto.newImageName())
                                .flatMap( uploadImageFileName ->
                                        imageService.deleteImage(dto.oldImageName())
                                        .thenReturn(infraMapper.toUpdatedChildImageResponseDto(dto))
                                )
                        )
                .doOnSuccess(childAccountUpdated ->
                        LOGGER.info(() -> String.format("Le compte est mise à jour %s", childAccountUpdated)))
                .doOnError(e ->
                        LOGGER.error("Erreur dans l'appel au service updateChildImage", e)));

    }

  @Override
  public Mono<UpdatedChildAccountResponseDto> updateChildName(UpdateChildNameRequestDto dto) {
    return txOperator.transactional(childAccountUseCaseAdapter.updateChildName(
                    dto.parentId(),
                    dto.childAccountId(),
                    dto.updateChildName()
            ))
            .doOnSuccess(childAccountUpdated ->
                    LOGGER.info(() -> String.format("Le compte est mise à jour %s", childAccountUpdated)))
            .doOnError(e ->
                    LOGGER.error("Erreur dans l'appel au service updateChildName", e));
  }

  @Override
    public Mono<UpdatedChildAccountResponseDto> updateChildMoneyAtPeriodStart(UpdateChildMoneyAtPeriodStartRequestDto dto) {
        return txOperator.transactional(childAccountUseCaseAdapter.updateChildMoneyAtPeriodStart(
                dto.parentId(),
                dto.childAccountId(),
                dto.updatedMoneyAtPeriodStart()
        ))
        .doOnSuccess(childAccountUpdated ->
                LOGGER.info(() -> String.format("Le compte est mise à jour %s", childAccountUpdated)))
        .doOnError(e ->
                LOGGER.error("Erreur dans l'appel au service updateChildMoneyAtPeriodStart", e));
    }

    @Override
    public Mono<UpdatedChildAccountResponseDto> addMoneyMovement(AddMoneyMovementRequestDto dto) {
        return txOperator.transactional(childAccountUseCaseAdapter.addMoneyMovement(
                        dto.parentId(),
                        dto.childAccountId(),
                        dto.reasonCode(),
                        dto.actionCode()
                ))
                .doOnSuccess(childAccountUpdated ->
                        LOGGER.info(() -> String.format("Le compte est mise à jour %s", childAccountUpdated)))
                .doOnError(e ->
                        LOGGER.error("Erreur dans l'appel au service addMoneyMovement", e));
    }

    @Override
    public Mono<UpdatedChildAccountResponseDto> reinitializeRemainingMoney(long parentId, long childAccountId) {
        return txOperator.transactional(childAccountUseCaseAdapter.reinitializeRemainingMoney(parentId, childAccountId))
                .doOnSuccess(childAccountUpdated ->
                        LOGGER.info(() -> String.format("Le compte est mise à jour %s", childAccountUpdated)))
                .doOnError(e ->
                        LOGGER.error("Erreur dans l'appel au service reinitializeRemainingMoney", e));
    }

    @Override
    public Mono<ImageStreaming> streamChildImage(long parentId, long childAccountId, String childImageName) {
        return txOperator.transactional(childAccountUseCaseAdapter.streamChildImage(parentId, childAccountId)
                    .flatMap(imageExtensionToStream -> {
                        var childImageNameWithExtension = String.format("%s.%s",childImageName, imageExtensionToStream);
                        Flux<DataBuffer> stream = imageService.streamImage(childImageNameWithExtension);
                        MediaType streamingMediaType = infraMapper.toMediaTypeFromExtension(imageExtensionToStream);
                        return Mono.just(new ImageStreaming(streamingMediaType, stream));
                    })
                )
                .doOnSuccess( imageStreaming->
                        LOGGER.info(() -> String.format("L'image suivante a été streamé: %s", childImageName)))
                .doOnError(e ->
                        LOGGER.error("Erreur dans l'appel au service addMoneyMovement", e));
    }

  @Override
  public Mono<UpdatedChildAccountResponseDto> desactivateChildAccount(Long parentId, Long childAccountId) {
    return txOperator.transactional(childAccountUseCaseAdapter.desactivateChildAccount(parentId, childAccountId))
            .doOnSuccess(childAccountUpdated ->
                    LOGGER.info(() -> String.format("Le compte est mise à jour %s", childAccountUpdated)))
            .doOnError(e ->
                    LOGGER.error("Erreur dans l'appel au service reinitializeRemainingMoney", e));
  }

  @Override
  public Mono<DataBuffer> generateQrCode(Long parentId, Long childAccountId) {
      // Mise a jour du endPount avec id du compte
      var endPointWithChildAccountId = String.format(qrCodeChildAccountUrl, childAccountId);

      // URL d'acces pour le qrCode
      var urlToDisplayInQrCode = String.format("%s%s", frontEndBaseUrl, endPointWithChildAccountId);
    return txOperator.transactional(childAccountUseCaseAdapter.generateQrCode(parentId, childAccountId, urlToDisplayInQrCode))
            .doOnSuccess(dataBuffer ->
                    LOGGER.info(() -> String.format("Stream du QR code du compte d'argent de poche ok, compte enfant %s ", childAccountId)))
            .doOnError(e ->
                    LOGGER.error("Erreur dans l'appel au service generateQrCode", e));
  }

  @Override
  public Mono<DisplayChildAccountInfoResponseDto> displayChildAccountInfo(Long childAccountId) {
    return txOperator.transactional(childAccountUseCaseAdapter.displayChildAccountInfo(childAccountId)
        .doOnSuccess(childAccount -> LOGGER.info(() -> String.format("Réussite de la récupération des données du compte enfant %s", childAccount.childAccountIdentity())))
        .doOnError(e -> LOGGER.error("Erreur dans l'appel au service loadChildAccount", e)));
  }
}
