package com.ctoutweb.argenDePoche.infra.repository;

import com.ctoutweb.argenDePoche.infra.repository.entity.ChildAccountMoneyMovementCodeEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface ChildAccountMoneyMovementCodeRepository extends ReactiveCrudRepository<ChildAccountMoneyMovementCodeEntity, Long> {

  /**
   * Recherche des données relatif a un compte d'argent de poche et d'un mouvement d'argent
   *
   * @param movementReasonId Lidentification du mouvement d'argent
   * @param childAccountId L'identifiant du compte d'argent de poche
   *
   * @return ChildAccountMoneyMovementCodeEntity
   */
  Mono<ChildAccountMoneyMovementCodeEntity> findFirstByMovementCodeIdAndChildAccountId(int movementReasonId, long childAccountId);

  /**
   * Suppression de tous les movements d'argent
   *
   * @param childAccountId L'identifiant du compte d'argent de poche
   *
   * @return Void
   */
  Mono<Void> deleteAllByChildAccountId(long childAccountId);

}
