package com.ctoutweb.argenDePoche.infra.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table(schema = "sc_argent_de_poche", name = "child_account")
public class ChildAccountEntity extends Temporal {
    @Id
    private Long id;

    @Column("family_account_id")
    private Long familyAccountId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFamilyAccountId() {
        return familyAccountId;
    }

    public void setFamilyAccountId(Long familyAccountId) {
        this.familyAccountId = familyAccountId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ChildAccountEntity that = (ChildAccountEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(familyAccountId, that.familyAccountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, familyAccountId);
    }

    @Override
    public String toString() {
        return "ChildAccountEntity{" +
                "id=" + id +
                ", familyAccountIdentity=" + familyAccountId +
                '}';
    }
}
