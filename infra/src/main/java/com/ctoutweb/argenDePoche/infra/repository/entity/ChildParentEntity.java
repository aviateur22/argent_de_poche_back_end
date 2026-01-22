package com.ctoutweb.argenDePoche.infra.repository.entity;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "child_parent", schema = "sc_argent_de_poche")
public class ChildParentEntity extends Temporal {

    private Long id;

    @Column("parent_id")
    private Long parentId;

    @Column("child_id")
    private Long childId;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

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

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChildParentEntity that = (ChildParentEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(parentId, that.parentId) && Objects.equals(childId, that.childId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentId, childId);
    }

    @Override
    public String toString() {
        return "ChildParentEntity{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", childId=" + childId +
                '}';
    }
}
