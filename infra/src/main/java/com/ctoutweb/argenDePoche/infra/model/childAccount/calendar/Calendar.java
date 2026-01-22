package com.ctoutweb.argenDePoche.infra.model.childAccount.calendar;

import java.time.LocalDate;

public record Calendar(
        PeriodSubscription periodSubscription,
        LocalDate periodStartDay,
        LocalDate periodEndDay
) {
}
