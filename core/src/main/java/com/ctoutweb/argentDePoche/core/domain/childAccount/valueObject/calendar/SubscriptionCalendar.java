package com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.calendar;

import com.ctoutweb.argentDePoche.core.domain.exception.CalendarException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;

public record SubscriptionCalendar(LocalDate actualDay, PeriodSubscription periodSubscription, LocalDate startDay, LocalDate endDay) {

    public SubscriptionCalendar {
        if(startDay.isAfter(endDay))
            throw new CalendarException("Erreur de date de calendrier");
    }

    private SubscriptionCalendar withUpdatedCalendarDate(
            PeriodSubscription updatedSubscription,
            LocalDate updatedStartDate,
            LocalDate updatedEndDate)  {
                return new SubscriptionCalendar(
                        this.actualDay,
                        updatedSubscription,
                        updatedStartDate,
                        updatedEndDate
                );
    }

    public SubscriptionCalendar updateCalendarSubscription(PeriodSubscription updatedPeriodSubscription) {
        return withUpdatedCalendarDate(
                updatedPeriodSubscription,
                loadStartDate(actualDay, updatedPeriodSubscription),
                loadEndDay(actualDay, updatedPeriodSubscription)
        );
    }

    /**
     * Factory permattant d'initialiser le calendrier d'argent de poche
     *
     * @param createdAccountDate Date de creation du compte
     * @param defaultPeriodSubscription Period par default de selection de l'agent de poche
     *
     * @return SubscriptionCalendar
     */
    public static SubscriptionCalendar created(LocalDate createdAccountDate, PeriodSubscription defaultPeriodSubscription) {
        var startDay = loadStartDate(createdAccountDate, defaultPeriodSubscription);
        var endDay =  loadEndDay(createdAccountDate, defaultPeriodSubscription);

        return new SubscriptionCalendar(createdAccountDate, defaultPeriodSubscription, startDay, endDay);
    }

    private static LocalDate loadEndDay(LocalDate actualDay, PeriodSubscription periodDuration) {
        return switch (periodDuration) {
            case WEEK -> actualDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
            case MONTH -> YearMonth.from(actualDay).atEndOfMonth();
        };
    }

    private static LocalDate loadStartDate(LocalDate actualDay, PeriodSubscription periodDuration) {
        return switch (periodDuration) {
            case WEEK -> actualDay.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            case MONTH -> YearMonth.from(actualDay).atDay(1);
        };
    }
}
