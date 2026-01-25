package com.ctoutweb.argenDePoche.infra.repository;

import com.ctoutweb.argenDePoche.infra.repository.entity.ChildEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ChildRepository extends ReactiveCrudRepository<ChildEntity, Long> {

  /**
   * Recherche d'un ChildEntity apparteant à un compte familiale
   *
   * @param childAccountId L'identifiant technqieu du compte familliale
   *
   * @return Mono<ChildEntity>
   */
  Mono<ChildEntity> findByChildAccountId(long childAccountId);
}
