package com.ctoutweb.argenDePoche.infra.repository;

import com.ctoutweb.argenDePoche.infra.repository.entity.JwtEntity;
import com.ctoutweb.argenDePoche.infra.repository.entity.ParentEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
public interface JwtRepository extends ReactiveCrudRepository<JwtEntity, Long> {
    /**
     * Suppression de tous les JWT liée a un identifiant vendeur
     *
     * @param parent Le vendeur dont on supprime le JWT
     */
    @Transactional
    @Modifying
    Mono<Void> deleteByParentId(ParentEntity parent);

    /**
     * Recherche d'un JWT par son identifiant
     *
     * @param jwtId L'identifiant du JWT a trouver
     *
     * @return Optional Les données du JWT ou optional.empty
     */
    Mono<JwtEntity> findFirstByJwtId(String jwtId);
}
