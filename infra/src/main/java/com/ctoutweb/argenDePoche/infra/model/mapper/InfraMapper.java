package com.ctoutweb.argenDePoche.infra.model.mapper;

import com.ctoutweb.argenDePoche.infra.model.dto.UpdatedChildImageDto;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.AddMoneyMovementRequestDto;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.UpdatedChildAccountResponseDto;
import com.ctoutweb.argentDePoche.application.port.AddMoneyMovementReason;
import org.springframework.stereotype.Component;

/**
 * Mapper utilisée uniquement pour le layer Infra.
 *
 * Cette class ne map pas vers des objets provenant vers le layer Application / Core
 * Mais des objets de l'infra peuvent implementer des interfaces des layers Application / Core
 */
@Component
public class InfraMapper {

  /**
   * Map UpdatedChildImageDto vers UpdatedChildAccountResponseDto
   *
   * @param dto Les données recu sur l'image mise a jour
   *
   * @return UpdatedChildAccountResponseDto
   */
  public UpdatedChildAccountResponseDto toUpdatedChildImageResponseDto(UpdatedChildImageDto dto) {
    return new UpdatedChildAccountResponseDto(dto.childAccountId());
  }

  /**
   * Map un AddMoneyMovementRequestDto vers un objet implementant AddMoneyMovement
   *
   * @param dto Les données d'ajout du mouvement d'argent
   *
   * @return AddMoneyMovement
   */
  public AddMoneyMovementReason toAddMoneyMovement(AddMoneyMovementRequestDto dto) {
    return null;
  }
}
