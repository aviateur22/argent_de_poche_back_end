package com.ctoutweb.argenDePoche.infra.model.childAccount.moneyMovement;

import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MoneyMovement(
        BigDecimal fluctuationPrice,
        MovementActionType action,
        MovementReason reason,
        LocalDateTime occurredAt,
        ParentIdentity initiatedByParent,
        long parentAddingMoneyMovement
) {
}
