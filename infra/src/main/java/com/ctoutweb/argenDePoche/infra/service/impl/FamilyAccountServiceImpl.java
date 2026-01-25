package com.ctoutweb.argenDePoche.infra.service.impl;

import com.ctoutweb.argenDePoche.infra.adapter.primaryAdapter.FamilyAccountManagerAdapter;
import com.ctoutweb.argenDePoche.infra.model.dto.CreateFamilyAccountRequestDto;
import com.ctoutweb.argenDePoche.infra.model.dto.CreateFamilyAccountResponseDto;
import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToDtoMapper;
import com.ctoutweb.argenDePoche.infra.model.dto.familyAccountResponse.FamilyAccountResponseDto;
import com.ctoutweb.argenDePoche.infra.service.AuthService;
import com.ctoutweb.argenDePoche.infra.service.FamilyAccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Service
public class FamilyAccountServiceImpl implements FamilyAccountService {

    private final AuthService authService;
    private final ToDtoMapper toDtoMapper;
    private final FamilyAccountManagerAdapter familyAccountManagerAdapter;
    private final TransactionalOperator txOperator;

  public FamilyAccountServiceImpl(AuthService authService, ToDtoMapper toDtoMapper, FamilyAccountManagerAdapter familyAccountManagerAdapter, TransactionalOperator txOperator) {
    this.authService = authService;
    this.toDtoMapper = toDtoMapper;
    this.familyAccountManagerAdapter = familyAccountManagerAdapter;
    this.txOperator = txOperator;
  }

  @Override
    public Mono<CreateFamilyAccountResponseDto> createFamilyAccount(CreateFamilyAccountRequestDto dto) {

     return txOperator.transactional(
         // Enregistrement
          authService.registerParent(dto.email(), dto.parentName(), dto.password())
          .flatMap(registerParentId ->
              familyAccountManagerAdapter.createFamilyAccount(registerParentId, dto.parentName())
              .map(familyAccountId ->toDtoMapper.mapToCreateFamilyAccountResponseDto(familyAccountId, registerParentId)))

      );
    }

  @Override
  public Mono<FamilyAccountResponseDto> loadFamily(Long parenId) {
    return txOperator.transactional(
            familyAccountManagerAdapter.loadFamilyAccount(parenId)
    );
  }
}
