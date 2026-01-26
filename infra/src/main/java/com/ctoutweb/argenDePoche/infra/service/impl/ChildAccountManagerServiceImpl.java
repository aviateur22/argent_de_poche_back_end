package com.ctoutweb.argenDePoche.infra.service.impl;

import com.ctoutweb.argenDePoche.infra.adapter.primaryAdapter.ChildAccountManagerAdapter;
import com.ctoutweb.argenDePoche.infra.model.dto.ChildAccountResponseDto;
import com.ctoutweb.argenDePoche.infra.model.dto.CreateChildAccountResponseDto;
import com.ctoutweb.argenDePoche.infra.model.dto.CreateChildAccountRequestDto;
import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToDtoMapper;
import com.ctoutweb.argenDePoche.infra.service.ChildAccountManagerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Service
public class ChildAccountManagerServiceImpl implements ChildAccountManagerService {
    private static final Logger LOGGER = LogManager.getLogger();

    @Value("${folder.image.path}")
    String saveImagePath;

    private final TransactionalOperator txOperator;

    private final ToDtoMapper toDtoMapper;

    private final ChildAccountManagerAdapter childAccountManager;

    public ChildAccountManagerServiceImpl(
            TransactionalOperator txOperator,
            ToDtoMapper toDtoMapper,
            ChildAccountManagerAdapter childAccountManager) {
        this.txOperator = txOperator;
        this.toDtoMapper = toDtoMapper;
        this.childAccountManager = childAccountManager;
    }

    @Override
    public Mono<CreateChildAccountResponseDto> createChildAccount(CreateChildAccountRequestDto dto) {
        return txOperator.transactional(
            Mono.defer(() ->
                childAccountManager.createChildAccount(dto.parentId(), dto.childName(), saveImagePath)
                        .map(toDtoMapper::mapToCreateChildAccountResponseDto)
                        .doOnSuccess(s -> LOGGER.info("Transaction réussie: {}", s))
                        .doOnError(e -> LOGGER.error("Erreur dans la transaction: ", e))
            ));
    }

    @Override
    public Mono<ChildAccountResponseDto> loadChildAccount(long parentId, long childAccountId) {
        return txOperator.transactional(childAccountManager.loadChildAccount(parentId, childAccountId)
                .doOnSuccess(s -> LOGGER.info("Transaction réussie: {}", s))
                .doOnError(e -> LOGGER.error("Erreur dans la transaction: ", e)));
    }
}
