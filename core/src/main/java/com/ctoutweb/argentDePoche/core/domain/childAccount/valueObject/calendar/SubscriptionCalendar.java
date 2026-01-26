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

    /**
     * Mise à jour de la period de souscription et recalcul des dates de debut et de fin de la période
     *
     * @param updatedPeriodSubscription La nouvelle période de soucscription
     *
     * @return Une nouvelle instance mise a jour de SubscriptionCalendar
     */
    public SubscriptionCalendar updateCalendarSubscription(PeriodSubscription updatedPeriodSubscription) {
        return withUpdatedCalendarDate(
                updatedPeriodSubscription,
                loadStartDate(actualDay, updatedPeriodSubscription),
                loadEndDay(actualDay, updatedPeriodSubscription)
        );
    }

    /**
     * Determine la prochaine periode de souscription d'argent de poche.
     * Les date de début et de fin de période sont mise à jour
     *
     * @return Une nouvelle instance avec les dates de début et de fin mise à jour
     */
    public SubscriptionCalendar nextCalendarPeriod(LocalDate activateNextPeriod) {
        // Récupération du 1er jour de la péeriode suivante
        var firstDayOfNextPeriod = this.endDay.plusDays(1);

        // Pour les comptes avec une periode mensuelle la periode suivante commence en fin de moi
        if(this.periodSubscription.equals(PeriodSubscription.MONTH) && !activateNextPeriod.equals(this.endDay))
            return this;

        // Mise à jour des dates
        var startDay = loadStartDate(firstDayOfNextPeriod, this.periodSubscription);
        var endDay =  loadEndDay(firstDayOfNextPeriod, this.periodSubscription);

        return new SubscriptionCalendar(firstDayOfNextPeriod, this.periodSubscription, startDay, endDay);
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
