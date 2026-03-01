package com.ctoutweb.argenDePoche.infra.repository.dto;

import org.springframework.data.relational.core.mapping.Column;

  /**
   * Récupération des roles d'un parent connecté
   * @param roleName La nom du role
   */
public record ParentRoleProjection(
      @Column("role")
      String roleName) {
}
