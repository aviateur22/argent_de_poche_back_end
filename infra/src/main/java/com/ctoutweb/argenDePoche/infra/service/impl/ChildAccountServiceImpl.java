package com.ctoutweb.argenDePoche.infra.service.impl;

import com.ctoutweb.argenDePoche.infra.adapter.primaryAdapter.ChildAccountUseCaseAdapter;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.ChildAccountResponseDto;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.CreateChildAccountResponseDto;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.CreateChildAccountRequestDto;
import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToDtoMapper;
import com.ctoutweb.argenDePoche.infra.model.dto.UpdatedChildImageDto;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.UpdatedChildImageResponseDto;
import com.ctoutweb.argenDePoche.infra.model.mapper.InfraMapper;
import com.ctoutweb.argenDePoche.infra.service.ChildAccountService;
import com.ctoutweb.argenDePoche.infra.service.ImageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Service("infraChildAccountServiceImpl")
public class ChildAccountServiceImpl implements ChildAccountService {
    private static final Logger LOGGER = LogManager.getLogger();

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
                childAccountUseCaseAdapter.createChildAccount(dto.parentId(), dto.childName())
                        .map(toDtoMapper::mapToCreateChildAccountResponseDto)
                        .doOnSuccess(s -> LOGGER.info("Transaction réussie: {}", s))
                        .doOnError(e -> LOGGER.error("Erreur dans la transaction: ", e))
            ));
    }

    @Override
    public Mono<ChildAccountResponseDto> loadChildAccount(long parentId, long childAccountId) {
        return txOperator.transactional(childAccountUseCaseAdapter.loadChildAccount(parentId, childAccountId)
                .doOnSuccess(s -> LOGGER.info("Transaction réussie: {}", s))
                .doOnError(e -> LOGGER.error("Erreur dans la transaction: ", e)));
    }

    @Override
    public Mono<UpdatedChildImageResponseDto> updateChildImage(FilePart childImageFile, long parentId, long childAccountId) {
        return txOperator.transactional(childAccountUseCaseAdapter.updateChildImage(parentId, childAccountId)
                        .flatMap(dto ->
                                imageService.saveImage(childImageFile, dto.newImageName())
                                .flatMap( uploadImageFileName ->
                                        imageService.deleteImage(dto.oldImageName())
                                        .thenReturn(infraMapper.toUpdatedChildImageResponseDto(dto))
                                )
                        )
                .doOnSuccess(s -> LOGGER.info("Transaction réussie: {}", s))
                .doOnError(e -> LOGGER.error("Erreur dans la transaction: ", e)));

    }
}
