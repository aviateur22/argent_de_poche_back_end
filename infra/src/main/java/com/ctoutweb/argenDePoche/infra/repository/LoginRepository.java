package com.ctoutweb.argenDePoche.infra.repository;

import com.ctoutweb.argenDePoche.infra.repository.entity.LoginEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface LoginRepository extends ReactiveCrudRepository<LoginEntity, Long> {
    /**
     * Récupération de toutes les connexions d'un client ordonnées de la connexion la plus récente à la plus vieille
     *
     * @param parent L'identifiant du parent e la personne en cours de connexion
     *
     * @return Une liste de toutes les connexions de l'utilisateur qui se connecte
     */
    Flux<LoginEntity> findByParentIdOrderByLoginAtDesc(long parent);
}
