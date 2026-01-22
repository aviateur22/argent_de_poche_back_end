package com.ctoutweb.argentDePoche.core.generateData;

import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.calendar.PeriodSubscription;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.calendar.SubscriptionCalendar;

import java.time.LocalDate;
import java.time.YearMonth;

public class GenerateCalendar {
    public SubscriptionCalendar generate() {
        LocalDate startDate = YearMonth.now().atDay(1);
        LocalDate endDate = YearMonth.now().atEndOfMonth();

        return new SubscriptionCalendar(
                LocalDate.now(),
                PeriodSubscription.MONTH,
                startDate,
                endDate);
    }
}
