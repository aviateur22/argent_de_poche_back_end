package com.ctoutweb.argenDePoche.infra.model.mapper;

import com.ctoutweb.argenDePoche.infra.model.dto.UpdatedChildImageDto;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.AddMoneyMovementRequestDto;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.UpdatedChildAccountResponseDto;
import com.ctoutweb.argentDePoche.application.port.AddMoneyMovementReason;
import org.springframework.http.MediaType;
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
   * Media type disponible d'une image a envoyer
   *
   * @param extension L'extension de l'image a streamer
   *
   * @return Le mediaType de l'image
   */
  public MediaType toMediaTypeFromExtension(String extension) {
    return switch (extension.toLowerCase()) {
      case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
      case "png"         -> MediaType.IMAGE_PNG;
      case "gif"         -> MediaType.IMAGE_GIF;
      case "webp"        -> MediaType.valueOf("image/webp");
      case "svg"         -> MediaType.valueOf("image/svg+xml");
      default            -> MediaType.APPLICATION_OCTET_STREAM;
    };
  }
}
