package com.ctoutweb.argenDePoche.infra.repository;

import com.ctoutweb.argenDePoche.infra.repository.entity.ChildAccountCalendarEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ChildCalendarRepository extends ReactiveCrudRepository<ChildAccountCalendarEntity, Long> {
  Mono<ChildAccountCalendarEntity> findFirstByChildAccountIdOrderByPeriodStartDayDesc(long childAccountId);
}
