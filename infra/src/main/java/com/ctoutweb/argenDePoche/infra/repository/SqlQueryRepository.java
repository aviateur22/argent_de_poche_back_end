package com.ctoutweb.argenDePoche.infra.repository;

import com.ctoutweb.argenDePoche.infra.repository.dto.ReasonMovementProjection;
import com.ctoutweb.argenDePoche.infra.repository.dto.FamilyAccountProjection;
import com.ctoutweb.argenDePoche.infra.repository.query.SqlQuery;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SqlQueryRepository extends ReactiveCrudRepository<FamilyAccountIdentity, Long> {

    @Query(SqlQuery.familyAccountQuery)
    Mono<FamilyAccountProjection> findFamilyAccount(@Param("parentId") long parentId);

    @Query(SqlQuery.getAvailableMovementReasonsByChildAccount)
    Flux<ReasonMovementProjection> getMovementReasons(@Param("childAccountId") long childAccountId);

}
