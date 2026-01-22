package com.ctoutweb.argenDePoche.infra.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.Objects;

@Table(schema = "sc_argent_de_poche", name = "child_account_calendar")
public class ChildAccountCalendarEntity extends Temporal {
    @Id
    private Long id;

    @Column("child_account_id")
    private Long childAccountId;

    @Column("calendar_period")
    private String calendarPeriod;

    @Column("period_start_day")
    private LocalDate periodStartDay;

    @Column("period_end_day")
    private LocalDate periodEndDay;

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

    public String getCalendarPeriod() {
        return calendarPeriod;
    }

    public void setCalendarPeriod(String calendarPeriod) {
        this.calendarPeriod = calendarPeriod;
    }

    public LocalDate getPeriodStartDay() {
        return periodStartDay;
    }

    public void setPeriodStartDay(LocalDate periodStartDay) {
        this.periodStartDay = periodStartDay;
    }

    public LocalDate getPeriodEndDay() {
        return periodEndDay;
    }

    public void setPeriodEndDay(LocalDate periodEndDay) {
        this.periodEndDay = periodEndDay;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ChildAccountCalendarEntity calendar = (ChildAccountCalendarEntity) o;
        return Objects.equals(id, calendar.id) && Objects.equals(childAccountId, calendar.childAccountId) && Objects.equals(calendarPeriod, calendar.calendarPeriod) && Objects.equals(periodStartDay, calendar.periodStartDay) && Objects.equals(periodEndDay, calendar.periodEndDay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, childAccountId, calendarPeriod, periodStartDay, periodEndDay);
    }

    @Override
    public String toString() {
        return "ChildAccountCalendarEntity{" +
                "id=" + id +
                ", childAccountId=" + childAccountId +
                ", calendarPeriod='" + calendarPeriod + '\'' +
                ", periodStartDay=" + periodStartDay +
                ", periodEndDay=" + periodEndDay +
                '}';
    }
}
