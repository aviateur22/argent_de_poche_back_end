package com.ctoutweb.argenDePoche.infra.adapter.primaryAdapter;

import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToCoreMapper;
import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToDtoMapper;
import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToInfraMapper;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.familyAccountResponse.FamilyAccountResponseDto;
import com.ctoutweb.argentDePoche.application.api.FamilyAccountUseCase;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FamilyAccountUseCaseAdapter {
  private final FamilyAccountUseCase familyAccountUseCase;

  private final ToCoreMapper toCoreMapper;
  private final ToInfraMapper toInfraMapper;
  private final ToDtoMapper toDtoMapper;

  public FamilyAccountUseCaseAdapter(
          FamilyAccountUseCase familyAccountManager,
          ToCoreMapper toCoreMapper,
          ToInfraMapper toInfraMapper,
          ToDtoMapper toDtoMapper) {
    this.familyAccountUseCase = familyAccountManager;
    this.toCoreMapper = toCoreMapper;
    this.toInfraMapper = toInfraMapper;
    this.toDtoMapper = toDtoMapper;
  }

  /**
   * Creation du compte de famille
   *
   * @param parentId L'identifiant technique du parent créant le compte de famille.
   *                 Cette identifiant vient de l'enregistrement du email MDP
   * @param familyName Le nom de famille
   *
   * @return L'identifiant du compte de la famille qui est créé
   */
  public Mono<Long> createFamilyAccount(long parentId, String familyName) {
    var parentIdentity = toCoreMapper.toParentIdentity(parentId);
    return familyAccountUseCase.createFamilyAccount(parentIdentity, familyName)
            .map(toInfraMapper::toTechnicalId);
  }

  /**
   * Récupération des données sur un compte de famille
   *
   * @param parentId L'identitifant technique du parent faisant la demande
   *
   * @return Une instance FamilyAccountResponseDto
   */
  public Mono<FamilyAccountResponseDto> loadFamilyAccount(long parentId) {
    var parentIdentity = toCoreMapper.toParentIdentity(parentId);

    return familyAccountUseCase.loadFamilyAccount(parentIdentity)
            .map(familyDto -> toDtoMapper.mapToFamilyAccountResponseDto(familyDto));
  }
}
