package com.ctoutweb.argenDePoche.infra.repository;

import com.ctoutweb.argenDePoche.infra.repository.entity.ChildAccountMovementEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface MoneyMovementRepository extends ReactiveCrudRepository<ChildAccountMovementEntity, Long> {
    @Query("""
        SELECT *
        FROM sc_argent_de_poche.child_account_movement
        WHERE child_account_id = :childAccountId
          AND created_at >= :minDate
          AND created_at <= :maxDate
    """)
    Flux<ChildAccountMovementEntity> findByChildAccountIdAndDateRange(
            Long childAccountId,
            LocalDate minDate,
            LocalDate maxDate
    );
}
