package com.ctoutweb.argenDePoche.infra.repository;

import com.ctoutweb.argenDePoche.infra.repository.entity.ChildMoneyEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ChildMoneyRepository extends ReactiveCrudRepository<ChildMoneyEntity, Long> {

  /**
   * Récuperation de l'argent de poche par identifiant de la periode
   *
   * @param accountCalendarId L'identifiant de la période
   *
   * @return ChildMoneyEntity
   */
  Mono<ChildMoneyEntity> findByAccountCalendarId(long accountCalendarId);
}
