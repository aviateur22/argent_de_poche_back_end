package com.ctoutweb.argenDePoche.infra.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentRepository extends ReactiveCrudRepository<ParentRepository, Long> {
}
