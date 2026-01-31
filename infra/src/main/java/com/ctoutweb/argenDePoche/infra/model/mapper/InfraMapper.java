package com.ctoutweb.argenDePoche.infra.model.mapper;

import com.ctoutweb.argenDePoche.infra.model.dto.UpdatedChildImageDto;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.UpdatedChildImageResponseDto;
import org.springframework.stereotype.Component;

/**
 * Mapper utilisée uniquement pour le layer Infra. Aucun Mapping vers le layer Application / Core
 */
@Component
public class InfraMapper {
  public UpdatedChildImageResponseDto toUpdatedChildImageResponseDto(UpdatedChildImageDto dto) {
    return new UpdatedChildImageResponseDto(dto.childAccountId());
  }
}
