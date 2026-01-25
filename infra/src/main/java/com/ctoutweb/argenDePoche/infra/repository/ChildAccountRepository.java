package com.ctoutweb.argenDePoche.infra.repository;

import com.ctoutweb.argenDePoche.infra.repository.entity.ChildAccountEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ChildAccountRepository extends ReactiveCrudRepository<ChildAccountEntity, Long> {

  /**
   * Récupération d'un Flux de  ChildAccountEntity associé a un compte de famille
   *
   * @param familyAccountId L'identifiant technique du compte de famille
   *
   * @return Flux<ChildAccountEntity>
   */
  Flux<ChildAccountEntity> findAllByFamilyAccountId(long familyAccountId);
}
