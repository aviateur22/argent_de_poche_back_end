package com.ctoutweb.argenDePoche.infra.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.Objects;

@Table(schema = "sc_argent_de_poche", name = "child_account_money")
public class ChildMoneyEntity extends Temporal {
    @Id
    private Long id;

    @Column("child_account_id")
    private Long childAccountId;

    @Column("account_calendar_id")
    private Long accountCalendarId;

    @Column("money_at_period_start")
    private BigDecimal moneyAtPeriodStart;

    @Column("remaining_money")
    private BigDecimal remainingMoney;

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

    public BigDecimal getMoneyAtPeriodStart() {
        return moneyAtPeriodStart;
    }

    public void setMoneyAtPeriodStart(BigDecimal moneyAtPeriodStart) {
        this.moneyAtPeriodStart = moneyAtPeriodStart;
    }

    public BigDecimal getRemainingMoney() {
        return remainingMoney;
    }

    public void setRemainingMoney(BigDecimal remainingMoney) {
        this.remainingMoney = remainingMoney;
    }

    public Long getAccountCalendarId() {
        return accountCalendarId;
    }

    public void setAccountCalendarId(Long accountCalendarId) {
        this.accountCalendarId = accountCalendarId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ChildMoneyEntity that = (ChildMoneyEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(childAccountId, that.childAccountId) && Objects.equals(accountCalendarId, that.accountCalendarId) && Objects.equals(moneyAtPeriodStart, that.moneyAtPeriodStart) && Objects.equals(remainingMoney, that.remainingMoney);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, childAccountId, accountCalendarId, moneyAtPeriodStart, remainingMoney);
    }

    @Override
    public String toString() {
        return "ChildMoneyEntity{" +
                "id=" + id +
                ", childAccountId=" + childAccountId +
                ", accountCalendarId=" + accountCalendarId +
                ", moneyAtPeriodStart=" + moneyAtPeriodStart +
                ", remainingMoney=" + remainingMoney +
                '}';
    }
}
