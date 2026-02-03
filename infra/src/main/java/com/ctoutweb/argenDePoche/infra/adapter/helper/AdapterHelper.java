package com.ctoutweb.argenDePoche.infra.adapter.helper;

import com.ctoutweb.argenDePoche.infra.model.dto.childAccount.calendar.PeriodSubscription;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;

@Component
public class AdapterHelper {
  public LocalDate loadEndDay(LocalDate actualDay, PeriodSubscription periodDuration) {
    return switch (periodDuration) {
      case WEEK -> actualDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
      case MONTH -> YearMonth.from(actualDay).atEndOfMonth();
    };
  }

  public LocalDate loadStartDate(LocalDate actualDay, PeriodSubscription periodDuration) {
    return switch (periodDuration) {
      case WEEK -> actualDay.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
      case MONTH -> YearMonth.from(actualDay).atDay(1);
    };
  }
}
