package com.ctoutweb.argenDePoche.infra.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Table(schema = "sc_argent_de_poche", name = "child_account_movement")
public class MoneyMovementEntity extends Temporal {
    @Id
    private Long id;

    @Column("child_account_id")
    private Long childAccountId;

    @Column("child_account_movement_code_id")
    private Long childAccountMovementCodeId;

    @Column("add_by")
    private Long addByParentId;

    @Column("movement_action_code")
    private String movementActionCode;

    @Column("movement_add_at")
    private LocalDateTime movementAddAt;

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

    public Long getChildAccountMovementCodeId() {
        return childAccountMovementCodeId;
    }

    public void setChildAccountMovementCodeId(Long childAccountMovementCodeId) {
        this.childAccountMovementCodeId = childAccountMovementCodeId;
    }

    public Long getAddByParentId() {
        return addByParentId;
    }

    public void setAddByParentId(Long addByParentId) {
        this.addByParentId = addByParentId;
    }

    public String getMovementActionCode() {
        return movementActionCode;
    }

    public void setMovementActionCode(String movementActionCode) {
        this.movementActionCode = movementActionCode;
    }

    public LocalDateTime getMovementAddAt() {
        return movementAddAt;
    }

    public void setMovementAddAt(LocalDateTime movementAddAt) {
        this.movementAddAt = movementAddAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MoneyMovementEntity that = (MoneyMovementEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(childAccountId, that.childAccountId) && Objects.equals(childAccountMovementCodeId, that.childAccountMovementCodeId) && Objects.equals(addByParentId, that.addByParentId) && Objects.equals(movementActionCode, that.movementActionCode) && Objects.equals(movementAddAt, that.movementAddAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, childAccountId, childAccountMovementCodeId, addByParentId, movementActionCode, movementAddAt);
    }

    @Override
    public String toString() {
        return "MoneyMovementEntity{" +
                "id=" + id +
                ", childAccountId=" + childAccountId +
                ", childAccountMovementCodeId=" + childAccountMovementCodeId +
                ", addByParentId=" + addByParentId +
                ", movementActionTypeCode='" + movementActionCode + '\'' +
                ", movementAddAt=" + movementAddAt +
                '}';
    }
}
