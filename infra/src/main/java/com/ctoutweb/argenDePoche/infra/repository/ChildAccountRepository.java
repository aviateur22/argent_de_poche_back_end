package com.ctoutweb.argenDePoche.infra.repository;

import com.ctoutweb.argenDePoche.infra.repository.entity.ChildAccountEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ChildAccountRepository extends ReactiveCrudRepository<ChildAccountEntity, Long> {

  /**
   * Récupére l'ensembke des compte d'argent de poche associée a une famille et qui sont actif.
   *
   * @param familyAccountId L'identifiant technique du compte de famille
   *
   * @return Renvoie la liste des compte d'argent de poche d'une meme famille et qui sont actif
   */
  Flux<ChildAccountEntity> findAllByFamilyAccountIdAndIsAccountActiveTrue(long familyAccountId);

  /**
   * Recherche d'un compte par son identifiant et qui est actif
   *
   * @param childAccountId Le compte recherché
   *
   * @return Le compte d'argent de poche recherché
   *
   */
  Mono<ChildAccountEntity> findFirstByIdAndIsAccountActiveTrue(long childAccountId);
}
