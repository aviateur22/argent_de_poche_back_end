package com.ctoutweb.argenDePoche.infra.repository;

import com.ctoutweb.argenDePoche.infra.repository.entity.MovementReasonEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface MovementReasonCodeRepository extends ReactiveCrudRepository<MovementReasonEntity, Integer> {

  /**
   * Recherche des données par code
   *
   * @param movementReasonCode Le code de la raison du mouvement d'argent
   *
   * @return MovementCodeEntity
   */
  Mono<MovementReasonEntity> findFirstByMovementCode(String movementReasonCode);
}
