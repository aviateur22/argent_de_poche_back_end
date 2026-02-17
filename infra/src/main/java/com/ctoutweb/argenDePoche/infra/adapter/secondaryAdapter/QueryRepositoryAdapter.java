package com.ctoutweb.argenDePoche.infra.adapter.secondaryAdapter;

import com.ctoutweb.argenDePoche.infra.adapter.helper.AdapterHelper;
import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToCoreMapper;
import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToInfraMapper;
import com.ctoutweb.argenDePoche.infra.model.dto.childAccount.calendar.PeriodSubscription;
import com.ctoutweb.argenDePoche.infra.model.dto.childAccount.moneyMovement.MoneyMovementActionType;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.AvailableMovementReasonDto;
import com.ctoutweb.argenDePoche.infra.repository.*;
import com.ctoutweb.argenDePoche.infra.repository.entity.ChildAccountCalendarEntity;
import com.ctoutweb.argentDePoche.application.exception.CalendarPeriodNotFound;
import com.ctoutweb.argentDePoche.application.exception.ChildImageNotFindException;
import com.ctoutweb.argentDePoche.application.exception.ChildMoneyNotFoundException;
import com.ctoutweb.argentDePoche.application.exception.ChildNotFindException;
import com.ctoutweb.argentDePoche.application.query.dto.FamilyChildDto;
import com.ctoutweb.argentDePoche.application.query.dto.FamilyInformationDto;
import com.ctoutweb.argentDePoche.application.query.dto.ChildAccountDto;
import com.ctoutweb.argentDePoche.application.repository.QueryRepository;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Component
public class QueryRepositoryAdapter implements QueryRepository {

    private final SqlQueryRepository sqlQueryRepository;
    private final ToInfraMapper toInfraMapper;
    private final ToCoreMapper toCoreMapper;
    private final AdapterHelper adapterHelper;
    private final ParentFamilyAccountRepository parentFamilyAccountRepository;
    private final ChildAccountRepository childAccountRepository;
    private final ChildRepository childRepository;
    private final ChildImageRepository childImageRepository;
    private final FamilyRepository familyRepository;
    private final ChildCalendarRepository childCalendarRepository;
    private final ChildMoneyRepository childMoneyRepository;

  public QueryRepositoryAdapter(
          SqlQueryRepository sqlQueryRepository,
          ParentFamilyAccountRepository parentFamilyAccountRepository,
          ToInfraMapper toInfraMapper,
          ToCoreMapper toCoreMapper, AdapterHelper adapterHelper, ChildAccountRepository childAccountRepository, ChildRepository childRepository, ChildImageRepository childImageRepository, FamilyRepository familyRepository, ChildCalendarRepository childCalendarRepository, ChildMoneyRepository childMoneyRepository) {
    this.sqlQueryRepository = sqlQueryRepository;
    this.parentFamilyAccountRepository = parentFamilyAccountRepository;
    this.toInfraMapper = toInfraMapper;
    this.toCoreMapper = toCoreMapper;
    this.adapterHelper = adapterHelper;
    this.childAccountRepository = childAccountRepository;
    this.childRepository = childRepository;
    this.childImageRepository = childImageRepository;
    this.familyRepository = familyRepository;
    this.childCalendarRepository = childCalendarRepository;
    this.childMoneyRepository = childMoneyRepository;
  }

    @Override
    public Flux<FamilyChildDto> loadChildAccountsByFamily(FamilyAccountIdentity familyAccountIdentity) {

    return childAccountRepository.findAllByFamilyAccountId(toInfraMapper.toTechnicalId(familyAccountIdentity))
            .flatMap(childAccount ->
                childRepository.findByChildAccountId(childAccount.getId())
                        .flatMap(child ->
                            childImageRepository.findById(child.getChildImageId())
                                    .map(childImage ->
                                            toCoreMapper.toFamilyChildDto(
                                            childAccount.getId(),
                                            child.getNickname(),
                                            childImage.getImageName()
                                    ))
                        )
            );

    }

    @Override
    public Mono<ChildAccountDto> loadActiveChildMoneyAccount(ChildMoneyAccountIdentity childMoneyAccountId) {
        long childAccountId = toInfraMapper.toTechnicalId(childMoneyAccountId);

      // Date actuelle
      final var actualDate = LocalDate.now();

      // Periode d'argent de poche mensuelle
      LocalDate startMonthDate = adapterHelper.loadStartDate(actualDate, PeriodSubscription.MONTH);
      LocalDate endMonthDate = adapterHelper.loadEndDay(actualDate, PeriodSubscription.MONTH);

      // Periode d'argent de poche semaine
      LocalDate startWeekDate = adapterHelper.loadStartDate(actualDate, PeriodSubscription.WEEK);
      LocalDate endWeekDate = adapterHelper.loadEndDay(actualDate, PeriodSubscription.WEEK);

      return loadActiveChildAccountCalendar(childAccountId, startMonthDate, endMonthDate)
          .switchIfEmpty(loadActiveChildAccountCalendar(childAccountId, startWeekDate, endWeekDate))
          .switchIfEmpty(Mono.error(new CalendarPeriodNotFound("Il n'y a pas de periode mensuelle ou hebdomadaire associée a ce compte d'argent de poche")))
          .flatMap(activeAccountCalendar ->
                  loadChildAccount(childAccountId, activeAccountCalendar)
          );
    }

    @Override
    public Flux<FamilyInformationDto> loadFamilyAccountsByParent(ParentIdentity parentIdentity) {
      return parentFamilyAccountRepository
        .findAllByParentId(toInfraMapper.toTechnicalId(parentIdentity))
        .flatMap(familyAccount ->
            familyRepository.findByFamilyAccountId(familyAccount.getFamilyAccountId())
                    .map(family -> toCoreMapper.toFamilyInformationDto(family))
            );
  }

  /**
   * Recherche du compte d'argent fonction d'une date de début et de fin
   *
   * @return ChildAccountCalendarEntity
   */
  private Mono<ChildAccountCalendarEntity> loadActiveChildAccountCalendar(long childAccountId, LocalDate startDay, LocalDate endDay) {
    return childCalendarRepository.findFirstByChildAccountIdAndPeriodStartDayAndPeriodEndDay(childAccountId, startDay, endDay);
  }

  /**
   * Chargement des données de compte d'argent de poche
   *
   * @param childAccountId L'identifiant du compte d'argent de poche
   * @param activeAccountCalendar Les données de periode du compte d'argent de poche
   *
   * @return Renvoie le compte d'argent de poche
   */
  private Mono<ChildAccountDto> loadChildAccount(long childAccountId, ChildAccountCalendarEntity activeAccountCalendar) {
    return childRepository.findByChildAccountId(childAccountId)
            .switchIfEmpty(Mono.error(new ChildNotFindException("L'enfant associé au compte d'argent de poche n'est pas trouvé")))
            .flatMap(child -> childImageRepository.findById(child.getChildImageId())
                    .switchIfEmpty(Mono.error(new ChildImageNotFindException("L'image associé au compte d'argent de poche n'est pas trouvé")))
                    .flatMap( childImage ->
                            childMoneyRepository.findByAccountCalendarId(activeAccountCalendar.getId())
                                    .switchIfEmpty(Mono.error(new ChildMoneyNotFoundException("L'argent associé au compte d'argent de poche n'est pas trouvé")))
                                    .flatMap(childMoney -> {
                                      var childId = child.getId();
                                      var childName = child.getNickname();
                                      var imageName = childImage.getImageName();
                                      var moneyAtPeriodStart = childMoney.getMoneyAtPeriodStart();
                                      var moneyRemaining = childMoney.getRemainingMoney();
                                      var actualDate = LocalDate.now();
                                      var calendarStartDate = activeAccountCalendar.getPeriodStartDay();
                                      var calendarEndDate = activeAccountCalendar.getPeriodEndDay();
                                      var periodSubscription = activeAccountCalendar.getCalendarPeriod();
                                      return this.availableReasonMovements(childAccountId)
                                              .collectList()
                                              .map(availableMovementReasons  ->
                                                    toCoreMapper.toChildAccountDto(
                                                      childAccountId,
                                                      childId,
                                                      childName,
                                                      imageName,
                                                      moneyAtPeriodStart,
                                                      moneyRemaining,
                                                      actualDate,
                                                      calendarStartDate,
                                                      calendarEndDate,
                                                      periodSubscription,
                                                      availableMovementReasons
                                                    )
                                              );
                                    })
                    )


            );
  }

  /**
   * Renvoie la liste des reaison de mouvement d'argent disponible pour le compte
   *
   * @param childAccountId Le compte d'argent de poche
   *
   * @return
   */
  private Flux<AvailableMovementReasonDto> availableReasonMovements(long childAccountId) {
    return sqlQueryRepository.getMovementReasons(childAccountId)
      .map(  reasonMovement -> {
        var addMoney = MoneyMovementActionType.ADD;
        var removeMoney = MoneyMovementActionType.REMOVE;
        return new AvailableMovementReasonDto(
                reasonMovement.reasonName(),
                reasonMovement.reasonCode(),
                addMoney.actionCode(),
                removeMoney.actionCode()
        );
      });
  }
}
