package com.ctoutweb.argenDePoche.infra.repository;

import com.ctoutweb.argenDePoche.infra.repository.entity.FamilyEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface FamilyRepository extends ReactiveCrudRepository<FamilyEntity, Long> {
  Mono<FamilyEntity> findByFamilyAccountId(long familyAccountId);
}
