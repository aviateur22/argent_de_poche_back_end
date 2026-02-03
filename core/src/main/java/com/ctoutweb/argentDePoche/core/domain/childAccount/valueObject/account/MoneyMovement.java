package com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account;

import com.ctoutweb.argentDePoche.core.domain.exception.MoneyMovementException;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public record MoneyMovement(
        BigDecimal fluctuationPrice,
        MovementActionType action,
        MovementReason reason,
        LocalDateTime occurredAt,
        ParentIdentity initiatedByParent) {

    public MoneyMovement {
            Objects.requireNonNull(action, "L'action d'ajout / suppression de l'argent de poche ne peut pas être vide: " + this);
            Objects.requireNonNull(reason, "La raison de modification de l'argent de poche ne peut pas être vide: " + this);
            Objects.requireNonNull(fluctuationPrice, "Le montant du mouvement d'argent est obligatoire");
            Objects.requireNonNull(occurredAt, "La date d'ajout du movement d'argent ne peut pas être vide");


            if(fluctuationPrice.compareTo(BigDecimal.ZERO) < 0)
                throw new MoneyMovementException("Le mouvement d'argent ne peut pas être négatif");
    }

    /**
     * Creation d'un movement d'argent
     *
     * @param fluctuationPrice La valeur du mouvement d'argent
     * @param actionCode Le code d'action recu
     * @param reasonCode La code de la raison recu
     * @param initiatedByParent L'identity du parent faisant l'ajout
     *
     * @return
     */
    public static MoneyMovement create(
            BigDecimal fluctuationPrice,
            String actionCode,
            String reasonCode,
            ParentIdentity initiatedByParent) {
        LocalDateTime occurredAt = LocalDateTime.now();
        MovementReason movementReason = MovementReason.load(reasonCode);
        MovementActionType movementActionType = MovementActionType.load(actionCode);

        return new MoneyMovement(fluctuationPrice, movementActionType, movementReason, occurredAt, initiatedByParent);
    }
}
