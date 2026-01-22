package com.ctoutweb.argenDePoche.infra.model.childAccount;

import com.ctoutweb.argenDePoche.infra.model.childAccount.calendar.Calendar;
import com.ctoutweb.argenDePoche.infra.model.childAccount.moneyMovement.MoneyMovement;

import java.math.BigDecimal;
import java.util.List;

public record ChildAccountDto(
        long childAccountId,
        Child child,
        Calendar calendarSubscription,
        BigDecimal startMoney,
        BigDecimal remainingMoney,
        List<MoneyMovement> moneyMovements) {
}
