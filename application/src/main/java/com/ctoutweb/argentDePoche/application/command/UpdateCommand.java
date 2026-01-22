package com.ctoutweb.argentDePoche.application.command;

import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;

/**
 * Contrat pour les commande mettant un jour un aggregat
 * Il permettra de mutualiser les controls d'accés
 *
 * @see BaseCommandHandler
 */
public interface UpdateCommand<R> extends Command<R> {
    ChildMoneyAccountIdentity getChildAccountUpdated();
    ParentIdentity getParentUpdatedChildAccount();
}
