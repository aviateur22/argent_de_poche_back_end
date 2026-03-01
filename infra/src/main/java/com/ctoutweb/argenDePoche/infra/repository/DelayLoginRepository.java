package com.ctoutweb.argenDePoche.infra.repository;

import com.ctoutweb.argenDePoche.infra.repository.entity.DelayLoginEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface DelayLoginRepository extends ReactiveCrudRepository<DelayLoginEntity, Long> {
    /**
     * Recherche si une une connexion est désactivé pour un utilsateur en cours de connexion
     *
     * @param parentId L'indentifiant du parent
     *
     * @return Renvoie un optional des informations sur une connexion désactivé
     *          ou
     *          Un optional de empty si l'utilsateur n'a pas sa connexion de désactivée
     */
    Mono<DelayLoginEntity> findFirstByParentId(Long parentId);
}
