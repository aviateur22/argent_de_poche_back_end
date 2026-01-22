package com.ctoutweb.argenDePoche.infra.repository;

import com.ctoutweb.argenDePoche.infra.repository.entity.ChildMoneyEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildMoneyRepository extends ReactiveCrudRepository<ChildMoneyEntity, Long> {
}
