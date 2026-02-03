package com.ctoutweb.argenDePoche.infra.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table(schema = "sc_argent_de_poche", name = "movement_action_code")
public class MovementReasonEntity {
  @Id
  private Integer id;

  @Column("movement_code")
  String movementCode;

  @Column("movement_name")
  String movementName;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getMovementCode() {
    return movementCode;
  }

  public void setMovementCode(String movementCode) {
    this.movementCode = movementCode;
  }

  public String getMovementName() {
    return movementName;
  }

  public void setMovementName(String movementName) {
    this.movementName = movementName;
  }

  @Override
  public boolean equals(Object o) {

    if (o == null || getClass() != o.getClass()) return false;
    MovementReasonEntity that = (MovementReasonEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(movementCode, that.movementCode) && Objects.equals(movementName, that.movementName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, movementCode, movementName);
  }

  @Override
  public String toString() {
    return "MoneyMovementCode{" +
            "id=" + id +
            ", movementCode='" + movementCode + '\'' +
            ", movementName='" + movementName + '\'' +
            '}';
  }
}
