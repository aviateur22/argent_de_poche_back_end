package com.ctoutweb.argenDePoche.infra.repository;

import com.ctoutweb.argenDePoche.infra.repository.entity.ChildMoneyEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ChildMoneyRepository extends ReactiveCrudRepository<ChildMoneyEntity, Long> {
  Mono<ChildMoneyEntity> findByAccountCalendarId(long accountCalendarId);
}
