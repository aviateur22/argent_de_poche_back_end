package com.ctoutweb.argenDePoche.infra.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.Objects;

@Table(schema = "sc_argent_de_poche", name = "child_account_movement_code")
public class ChildAccountMoneyMovementCodeEntity extends Temporal {
  @Id
  private Long id;

  @Column("movement_code_id")
  private Integer movementCodeId;

  @Column("child_account_id")
  private Long childAccountId;

  @Column("movement_fluctuation_price")
  private BigDecimal movementFluctuationPrice;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getMovementCodeId() {
    return movementCodeId;
  }

  public void setMovementCodeId(Integer movementCodeId) {
    this.movementCodeId = movementCodeId;
  }

  public Long getChildAccountId() {
    return childAccountId;
  }

  public void setChildAccountId(Long childAccountId) {
    this.childAccountId = childAccountId;
  }

  public BigDecimal getMovementFluctuationPrice() {
    return movementFluctuationPrice;
  }

  public void setMovementFluctuationPrice(BigDecimal movementFluctuationPrice) {
    this.movementFluctuationPrice = movementFluctuationPrice;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    ChildAccountMoneyMovementCodeEntity that = (ChildAccountMoneyMovementCodeEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(movementCodeId, that.movementCodeId) && Objects.equals(childAccountId, that.childAccountId) && Objects.equals(movementFluctuationPrice, that.movementFluctuationPrice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id, movementCodeId, childAccountId, movementFluctuationPrice);
  }

  @Override
  public String toString() {
    return "ChildAccountMoneyMovementCodeEntity{" +
            "id=" + id +
            ", movementCodeId='" + movementCodeId + '\'' +
            ", childAccountId=" + childAccountId +
            ", movementFluctuationPrice=" + movementFluctuationPrice +
            '}';
  }
}
