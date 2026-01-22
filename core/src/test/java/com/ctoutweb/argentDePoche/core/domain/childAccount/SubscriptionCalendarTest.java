package com.ctoutweb.argentDePoche.core.domain.childAccount;

import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.calendar.PeriodSubscription;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.calendar.SubscriptionCalendar;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class SubscriptionCalendarTest {

    PeriodSubscription monthlyPeriodSubscription = PeriodSubscription.MONTH;
    PeriodSubscription weekPeriodSubscription = PeriodSubscription.WEEK;

    // Mois de décembre
    private final LocalDate startDayDecembre = LocalDate.of(2025, 12, 1);
    private final LocalDate endDateDecembre = LocalDate.of(2025, 12, 31);

    private final LocalDate startDayOctober = LocalDate.of(2025, 10, 13);
    private final LocalDate endDayOctober = LocalDate.of(2025, 10, 19);

    SubscriptionCalendar subscriptionCalendar;
    //

    @Test
    void update_subscription_to_week_on_monday() {
        /**
         * Given
         */
        //Lundi
        LocalDate actualDate = LocalDate.of(2025, 12, 1);
        subscriptionCalendar = new SubscriptionCalendar(actualDate, monthlyPeriodSubscription, startDayDecembre, endDateDecembre);

        /**
         * When
         */
        SubscriptionCalendar weeklySubscription = subscriptionCalendar.updateCalendarSubscription(PeriodSubscription.WEEK);

        /**
         * Then
         */
        LocalDate expectedStartDay = LocalDate.of(2025, 12, 1);
        LocalDate expectEndDay = LocalDate.of(2025, 12, 7);
        Assertions.assertEquals(actualDate, weeklySubscription.actualDay());
        Assertions.assertEquals(expectedStartDay, weeklySubscription.startDay());
        Assertions.assertEquals(expectEndDay, weeklySubscription.endDay());
        Assertions.assertEquals(PeriodSubscription.WEEK, weeklySubscription.periodSubscription());
    }

    @Test
    void update_subscription_to_week_on_tuesday() {
        /**
         * Given
         */
        // Mardi
        LocalDate actualDate = LocalDate.of(2025, 12, 2);
        subscriptionCalendar = new SubscriptionCalendar(actualDate, monthlyPeriodSubscription, startDayDecembre, endDateDecembre);

        /**
         * When
         */
        SubscriptionCalendar weeklySubscription = subscriptionCalendar.updateCalendarSubscription(PeriodSubscription.WEEK);

        /**
         * Then
         */
        LocalDate expectedStartDay = LocalDate.of(2025, 12, 1);
        LocalDate expectEndDay = LocalDate.of(2025, 12, 7);
        Assertions.assertEquals(actualDate, weeklySubscription.actualDay());
        Assertions.assertEquals(expectedStartDay, weeklySubscription.startDay());
        Assertions.assertEquals(expectEndDay, weeklySubscription.endDay());
        Assertions.assertEquals(PeriodSubscription.WEEK, weeklySubscription.periodSubscription());
    }

    @Test
    void update_subscription_to_week_on_sunday() {
        /**
         * Given
         */
        // Mardi
        LocalDate actualDate = LocalDate.of(2025, 12, 7);
        subscriptionCalendar = new SubscriptionCalendar(actualDate, monthlyPeriodSubscription, startDayDecembre, endDateDecembre);

        /**
         * When
         */
        SubscriptionCalendar weeklySubscription = subscriptionCalendar.updateCalendarSubscription(PeriodSubscription.WEEK);

        /**
         * Then
         */
        LocalDate expectedStartDay = LocalDate.of(2025, 12, 1);
        LocalDate expectEndDay = LocalDate.of(2025, 12, 7);
        Assertions.assertEquals(actualDate, weeklySubscription.actualDay());
        Assertions.assertEquals(expectedStartDay, weeklySubscription.startDay());
        Assertions.assertEquals(expectEndDay, weeklySubscription.endDay());
        Assertions.assertEquals(PeriodSubscription.WEEK, weeklySubscription.periodSubscription());
    }

    @Test
    void update_subscription_to_week_at_last_month_day() {
        /**
         * Given
         */
        // Mercredi
        LocalDate actualDate = LocalDate.of(2025, 12, 31);
        subscriptionCalendar = new SubscriptionCalendar(actualDate, monthlyPeriodSubscription, startDayDecembre, endDateDecembre);

        /**
         * When
         */
        SubscriptionCalendar weeklySubscription = subscriptionCalendar.updateCalendarSubscription(PeriodSubscription.WEEK);

        /**
         * Then
         */
        LocalDate expectedStartDay = LocalDate.of(2025, 12, 29);
        LocalDate expectEndDay = LocalDate.of(2026, 1, 4);
        Assertions.assertEquals(actualDate, weeklySubscription.actualDay());
        Assertions.assertEquals(expectedStartDay, weeklySubscription.startDay());
        Assertions.assertEquals(expectEndDay, weeklySubscription.endDay());
        Assertions.assertEquals(PeriodSubscription.WEEK, weeklySubscription.periodSubscription());
    }

    @Test
    void update_subscription_to_month_when_actual_day_is_first_day_of_month() {
        /**
         * Given
         */
        // Mercredi
        LocalDate actualDate = LocalDate.of(2025, 10, 1);
        subscriptionCalendar = new SubscriptionCalendar(actualDate, weekPeriodSubscription, startDayOctober, endDayOctober);

        /**
         * When
         */
        SubscriptionCalendar monthlySubscription = subscriptionCalendar.updateCalendarSubscription(PeriodSubscription.MONTH);

        /**
         * Then
         */
        LocalDate expectedStartDay = LocalDate.of(2025, 10, 1);
        LocalDate expectEndDay = LocalDate.of(2025, 10, 31);
        Assertions.assertEquals(actualDate, monthlySubscription.actualDay());
        Assertions.assertEquals(expectedStartDay, monthlySubscription.startDay());
        Assertions.assertEquals(expectEndDay, monthlySubscription.endDay());
        Assertions.assertEquals(PeriodSubscription.MONTH, monthlySubscription.periodSubscription());
    }

    @Test
    void update_subscription_to_month_when_actual_day_is_last_day_of_month() {
        /**
         * Given
         */
        // vendredi
        LocalDate actualDate = LocalDate.of(2025, 10, 31);
        subscriptionCalendar = new SubscriptionCalendar(actualDate, weekPeriodSubscription, startDayOctober, endDayOctober);

        /**
         * When
         */
        SubscriptionCalendar monthlySubscription = subscriptionCalendar.updateCalendarSubscription(PeriodSubscription.MONTH);

        /**
         * Then
         */
        LocalDate expectedStartDay = LocalDate.of(2025, 10, 1);
        LocalDate expectEndDay = LocalDate.of(2025, 10, 31);
        Assertions.assertEquals(actualDate, monthlySubscription.actualDay());
        Assertions.assertEquals(expectedStartDay, monthlySubscription.startDay());
        Assertions.assertEquals(expectEndDay, monthlySubscription.endDay());
        Assertions.assertEquals(PeriodSubscription.MONTH, monthlySubscription.periodSubscription());
    }

    @Test
    void update_subscription_to_month_when_actual_day_is_sunday() {
        /**
         * Given
         */
        // dimanche
        LocalDate actualDate = LocalDate.of(2025, 10, 19);
        subscriptionCalendar = new SubscriptionCalendar(actualDate, weekPeriodSubscription, startDayOctober, endDayOctober);

        /**
         * When
         */
        SubscriptionCalendar monthlySubscription = subscriptionCalendar.updateCalendarSubscription(PeriodSubscription.MONTH);

        /**
         * Then
         */
        LocalDate expectedStartDay = LocalDate.of(2025, 10, 1);
        LocalDate expectEndDay = LocalDate.of(2025, 10, 31);
        Assertions.assertEquals(actualDate, monthlySubscription.actualDay());
        Assertions.assertEquals(expectedStartDay, monthlySubscription.startDay());
        Assertions.assertEquals(expectEndDay, monthlySubscription.endDay());
        Assertions.assertEquals(PeriodSubscription.MONTH, monthlySubscription.periodSubscription());
    }

}
