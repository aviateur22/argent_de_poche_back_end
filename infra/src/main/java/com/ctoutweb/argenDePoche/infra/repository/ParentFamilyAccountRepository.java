package com.ctoutweb.argenDePoche.infra.repository;

import com.ctoutweb.argenDePoche.infra.repository.entity.ParentFamilyAccountEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ParentFamilyAccountRepository extends ReactiveCrudRepository<ParentFamilyAccountEntity, Long> {
  Flux<ParentFamilyAccountEntity> findAllByParentId(long parentId);
}
