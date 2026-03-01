package com.ctoutweb.argenDePoche.infra.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Table(schema = "sc_argent_de_poche", name = "delay_login")
public class DelayLoginEntity extends Temporal {
    @Id
    private Long id;

    @Column("parent_id")
    private Long parentId;

    @Column("delay_login_until")
    private LocalDateTime delayLoginUntil;

    public DelayLoginEntity(Long parentId, LocalDateTime delayLoginUntil) {
        this.parentId = parentId;
        this.delayLoginUntil = delayLoginUntil;
    }

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

    public LocalDateTime getDelayLoginUntil() {
        return delayLoginUntil;
    }

    public void setDelayLoginUntil(LocalDateTime delayLoginUntil) {
        this.delayLoginUntil = delayLoginUntil;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DelayLoginEntity that = (DelayLoginEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(parentId, that.parentId) && Objects.equals(delayLoginUntil, that.delayLoginUntil);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, parentId, delayLoginUntil);
    }

    @Override
    public String toString() {
        return "DelayLoginEntity{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", delayLoginUntil=" + delayLoginUntil +
                '}';
    }
}
