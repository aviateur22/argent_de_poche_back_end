package com.ctoutweb.argenDePoche.infra.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table(schema = "sc_argent_de_poche", name = "child_account_movement")
public class ChildAccountMovementEntity extends Temporal {
    @Id
    private Long id;

    @Column("child_account_id")
    private Long childAccountId;

    @Column("add_by")
    private Long addByParentId;

    @Column("fluctuation_price")
    private Integer fluctuationPrice;

    @Column("movement_action_type_code")
    private String movementActionTypeCode;

    @Column("movement_reason_code")
    private String movementReasonCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChildAccountId() {
        return childAccountId;
    }

    public void setChildAccountId(Long childAccountId) {
        this.childAccountId = childAccountId;
    }

    public Long getAddByParentId() {
        return addByParentId;
    }

    public void setAddByParentId(Long addByParentId) {
        this.addByParentId = addByParentId;
    }

    public Integer getFluctuationPrice() {
        return fluctuationPrice;
    }

    public void setFluctuationPrice(Integer fluctuationPrice) {
        this.fluctuationPrice = fluctuationPrice;
    }

    public String getMovementActionTypeCode() {
        return movementActionTypeCode;
    }

    public void setMovementActionTypeCode(String movementActionTypeCode) {
        this.movementActionTypeCode = movementActionTypeCode;
    }

    public String getMovementReasonCode() {
        return movementReasonCode;
    }

    public void setMovementReasonCode(String movementReasonCode) {
        this.movementReasonCode = movementReasonCode;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ChildAccountMovementEntity that = (ChildAccountMovementEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(childAccountId, that.childAccountId) && Objects.equals(addByParentId, that.addByParentId) && Objects.equals(fluctuationPrice, that.fluctuationPrice) && Objects.equals(movementActionTypeCode, that.movementActionTypeCode) && Objects.equals(movementReasonCode, that.movementReasonCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, childAccountId, addByParentId, fluctuationPrice, movementActionTypeCode, movementReasonCode);
    }

    @Override
    public String toString() {
        return "ChildAccountMovement{" +
                "id=" + id +
                ", childAccountId=" + childAccountId +
                ", addByParentId=" + addByParentId +
                ", fluctuationPrice=" + fluctuationPrice +
                ", movementActionTypeCode='" + movementActionTypeCode + '\'' +
                ", movementReasonCode='" + movementReasonCode + '\'' +
                '}';
    }
}
