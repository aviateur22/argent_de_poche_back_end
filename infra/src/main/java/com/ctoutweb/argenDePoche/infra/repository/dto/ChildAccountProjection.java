package com.ctoutweb.argenDePoche.infra.repository.dto;

import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ChildAccountProjection(
        @Column("child_account_id")
        long childAccountId,

        @Column("child_name")
        String childName,

        @Column("image_name")
        String imageName,

        @Column("calendar_period")
        String calendarPeriod,

        @Column("period_startday")
        LocalDate periodStartday,

        @Column("period_end_day")
        LocalDate periodEndDay,

        @Column("start_money")
        BigDecimal startMoney,

        @Column("remaining_money")
        BigDecimal remainingMoney) {
}
