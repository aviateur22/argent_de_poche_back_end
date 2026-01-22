package com.ctoutweb.argenDePoche.infra.repository;

import com.ctoutweb.argenDePoche.infra.repository.dto.ChildAccountProjection;
import com.ctoutweb.argenDePoche.infra.repository.dto.FamilyAccountProjection;
import com.ctoutweb.argenDePoche.infra.repository.dto.NextChilAccountGeneratedProjection;
import com.ctoutweb.argenDePoche.infra.repository.query.CmdRepositoryQuery;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CmdRepository extends ReactiveCrudRepository<FamilyAccountIdentity, Long> {

    @Query(CmdRepositoryQuery.childAccountQuery)
    Mono<ChildAccountProjection> findChildAccount(@Param("childAccountId") long childAccountId);

    @Query(CmdRepositoryQuery.familyAccountQuery)
    Mono<FamilyAccountProjection> findFamilyAccount(@Param("parentId") long parentId);

    @Query(CmdRepositoryQuery.generateNextChildAccountIdentificationQuery)
    Mono<NextChilAccountGeneratedProjection> generateNextChildAccountIdentification();

    @Query(CmdRepositoryQuery.registerNewChildAccount)
    Mono<Long> createChildAccount(
            @Param("childAccountId") long childAccountId,
            @Param("familyAccountId") long familyAccountId,
            @Param("childId") long childId,
            @Param("childName") String childName,
            @Param("childImage") String childImage
    );

}
