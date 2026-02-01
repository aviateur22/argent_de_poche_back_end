package com.ctoutweb.argenDePoche.infra.repository;

import com.ctoutweb.argenDePoche.infra.repository.entity.ChildAccountCalendarEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Repository
public interface ChildCalendarRepository extends ReactiveCrudRepository<ChildAccountCalendarEntity, Long> {

  /**
   * Renvoie la periode d'argent de poche dont la date de début est la plus récente
   *
   * @param childAccountId Le compte d'argent de poche
   *
   * @return ChildAccountCalendarEntity
   */
  Mono<ChildAccountCalendarEntity> findFirstByChildAccountIdOrderByPeriodStartDayDesc(long childAccountId);

  /**
   * Recherche de la periode d'argent de poche active
   * Les date passées en parametres sont soient:
   * - des dates du mois actuel
   * - ou de la semaine actuelle
   *
   * @param childAccountId Le compte d'argent de poche
   * @param periodStart La date de debut de la periode (mois actuelle ou semaine actuelle)
   * @param periodEndDay La date de fin de la periode (mois actuelle ou semaine actuelle)
   *
   * @return ChildAccountCalendarEntity
   */
  Mono<ChildAccountCalendarEntity> findFirstByChildAccountIdAndPeriodStartDayAndPeriodEndDay(long childAccountId, LocalDate periodStart, LocalDate periodEndDay);
}
