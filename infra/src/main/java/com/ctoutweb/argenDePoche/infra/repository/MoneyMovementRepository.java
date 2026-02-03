package com.ctoutweb.argenDePoche.infra.repository;

import com.ctoutweb.argenDePoche.infra.repository.entity.MoneyMovementEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface MoneyMovementRepository extends ReactiveCrudRepository<MoneyMovementEntity, Long> {
    @Query("""
        SELECT *
        FROM sc_argent_de_poche.child_account_movement
        WHERE child_account_id = :childAccountId
          AND movement_add_at::date >= :minDate
          AND movement_add_at::date <= :maxDate
    """)
    Flux<MoneyMovementEntity> findByChildAccountIdAndDateRange(
            Long childAccountId,
            LocalDate minDate,
            LocalDate maxDate
    );


    /**
     * Suppression des movements d'argent par compte d'argent de poche
     *
     * @param childAccountId L'identifiant du compte d'argent de poche
     *
     * @return Void
     */
    Mono<Void> deleteAllByChildAccountId(long childAccountId);
}
