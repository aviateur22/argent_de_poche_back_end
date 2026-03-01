package com.ctoutweb.argenDePoche.infra.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table(schema = "sc_argent_de_poche", name = "role_parent")
public class RoleParentEntity extends Temporal {
    @Id
    private Long id;

    @Column("parent_id")
    private Long parentId;

    @Column("role_id")
    private Integer roleId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RoleParentEntity that = (RoleParentEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(parentId, that.parentId) && Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, parentId, roleId);
    }

    @Override
    public String toString() {
        return "RoleParentEntity{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", roleId=" + roleId +
                '}';
    }
}
