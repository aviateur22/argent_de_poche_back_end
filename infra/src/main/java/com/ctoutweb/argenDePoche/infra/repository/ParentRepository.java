package com.ctoutweb.argenDePoche.infra.repository;

import com.ctoutweb.argenDePoche.infra.repository.entity.ParentEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ParentRepository extends ReactiveCrudRepository<ParentEntity, Long> {

  /**
   * Recherche un parent par son email
   *
   * @param email l'email du parent
   *
   * @return Le parent correspondant à l'email
   *
   */
  Mono<ParentEntity> findByEmail( String email);
}
