package com.ctoutweb.argenDePoche.infra.service;

import com.ctoutweb.argenDePoche.infra.model.dto.controller.CreateFamilyAccountRequestDto;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.CreateFamilyAccountResponseDto;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.familyAccountResponse.FamilyAccountResponseDto;
import reactor.core.publisher.Mono;

public interface FamilyAccountService {
  /**
   * Création d'une famille et d'un parent
   *
   * @param dto Contient les données necessaire à la creation du nouveau compte famililale
   *
   * @return Le nouveau compte de famille
   */
    Mono<CreateFamilyAccountResponseDto> createFamilyAccount(CreateFamilyAccountRequestDto dto);

  /**
   * Récupération des données liées à une famille
   *
   * @param parenId Identifiant tecnhique du parent initiant la demande
   *
   * @return Les données sur la famille
   */
  Mono<FamilyAccountResponseDto> loadFamily(Long parenId);
}
