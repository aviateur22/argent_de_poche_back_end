package com.ctoutweb.argentDePoche.application.command.helper;

import com.ctoutweb.argentDePoche.application.configuration.annotation.CoreService;
import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccount;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.calendar.PeriodSubscription;
import com.ctoutweb.argentDePoche.core.domain.exception.ChildMoneyAccountException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Class permettant la réutilisation d'un chargement d'un aggregat ChildMoneyAccount
 * Il sera réutilisé dans les CommandHandler
 */
@CoreService
public final class LoaderChildAccount {

    private final CommandRepository commandRepository;

    public LoaderChildAccount(CommandRepository commandRepository) {
        this.commandRepository = commandRepository;
    }

    /**
     * Renvoie les données pour un compte d'un enfant
     *
     * @param childMoneyAccountId Le compte de l'enfant à charger
     *
     * @return ChildMoneyAccount
     */
    public Mono<ChildMoneyAccount> load(ChildMoneyAccountIdentity childMoneyAccountId) {
        // Chargement des données du compte avec une profondeur du mois actuel
        LocalDate startDate = YearMonth.now().atDay(1);
        LocalDate endDate = YearMonth.now().atEndOfMonth();

        // Chargement des données
        return commandRepository.loadChildMoneyAccountAggregate(childMoneyAccountId, startDate, endDate)
                .switchIfEmpty(Mono.error(new ChildMoneyAccountException("Il n'y a pas de données associé à ce compte")))
                .flatMap(monthChildMoneyAccount -> {
                    // Si PeriodSubscription.WEEK alors filtrage des données avec une profondeur de la semaine en cours
                    if(monthChildMoneyAccount.getCalendarSubscription().periodSubscription().equals(PeriodSubscription.WEEK))
                        return Mono.just(monthChildMoneyAccount.loadWeekPeriodSubscription());

                    return Mono.just(monthChildMoneyAccount);
                });
    }
}
