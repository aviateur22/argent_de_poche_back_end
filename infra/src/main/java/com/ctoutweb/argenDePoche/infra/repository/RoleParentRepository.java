package com.ctoutweb.argenDePoche.infra.repository;

import com.ctoutweb.argenDePoche.infra.repository.dto.ParentRoleProjection;
import com.ctoutweb.argenDePoche.infra.repository.entity.RoleParentEntity;
import com.ctoutweb.argenDePoche.infra.repository.query.SqlQuery;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface RoleParentRepository extends ReactiveCrudRepository<RoleParentEntity, Long> {

  /**
   * Récupération de tous les roles associés à un parent
   *
   * @param parentId L'identifiant du parent
   *
   * @return Les roles du parents
   */
  Flux<RoleParentEntity> findByParentId(long parentId);

  /**
   * Récupération des roles associé a un parent
   *
   * @param parentId L'identifiant du parent
   *
   * @return Les noms des roles
   */
  @Query(SqlQuery.getAllParentRoles)
  Flux<ParentRoleProjection> getParentRoles(@Param("parentId") long parentId);


}
