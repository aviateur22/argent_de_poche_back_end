package com.ctoutweb.argenDePoche.infra.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(schema = "sc_argent_de_poche", name = "role_parent")
public class RoleParentEntity {
    @Id
    private Long id;

    @Column("parent_id")
    private Long parentId;

    @Column("role_id")
    private Integer roleId;
}
