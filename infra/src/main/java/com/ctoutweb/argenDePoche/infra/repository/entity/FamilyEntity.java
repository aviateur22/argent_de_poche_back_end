package com.ctoutweb.argenDePoche.infra.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table(schema = "sc_argent_de_poche", name = "family")
public class FamilyEntity extends Temporal {
  @Id
  private Long id;

  @Column("family_account_id")
  private Long familyAccountId;

  private String name;

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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    FamilyEntity that = (FamilyEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(familyAccountId, that.familyAccountId) && Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id, familyAccountId, name);
  }

  @Override
  public String toString() {
    return "FamilyEntity{" +
            "id=" + id +
            ", familyAccountIdentity=" + familyAccountId +
            ", name='" + name + '\'' +
            '}';
  }
}
