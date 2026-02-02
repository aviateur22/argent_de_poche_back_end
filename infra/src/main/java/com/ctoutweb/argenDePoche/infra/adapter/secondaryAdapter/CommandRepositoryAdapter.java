package com.ctoutweb.argenDePoche.infra.adapter.secondaryAdapter;

import com.ctoutweb.argenDePoche.infra.adapter.helper.AdapterHelper;
import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToCoreMapper;
import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToInfraMapper;
import com.ctoutweb.argenDePoche.infra.model.dto.AddMoneyMovementReasonDto;
import com.ctoutweb.argenDePoche.infra.model.dto.childAccount.calendar.PeriodSubscription;
import com.ctoutweb.argenDePoche.infra.repository.*;
import com.ctoutweb.argenDePoche.infra.repository.entity.*;
import com.ctoutweb.argentDePoche.application.exception.CalendarPeriodNotFound;
import com.ctoutweb.argentDePoche.application.exception.FamilyAccountForbiddenException;
import com.ctoutweb.argentDePoche.application.port.AddMoneyMovementReason;
import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccount;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account.MoneyMovement;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccount;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class CommandRepositoryAdapter implements CommandRepository {
  private static final Logger LOGGER = LogManager.getLogger();

    private final SqlQueryRepository cmdRepository;
    private final ChildMoneyRepository childMoneyRepository;
    private final ChildAccountRepository childAccountRepository;
    private final ChildRepository childRepository;
    private final ChildImageRepository childImageRepository;
    private final FamilyAccountRepository familyAccountRepository;
    private final FamilyRepository familyRepository;
    private final ParentFamilyAccountRepository parentFamilyAccountRepository;
    private final ChildCalendarRepository childCalendarRepository;
    private final MoneyMovementRepository moneyMovementRepository;
    private final ChildAccountMoneyMovementCodeRepository childAccountMoneyMovementCodeRepository;
    private final MovementReasonCodeRepository movementReasonRepository;

    private final ToCoreMapper toCoreIdentity;
    private final ToInfraMapper toInfraMapper;
    private final AdapterHelper adapterHelper;

    public CommandRepositoryAdapter(
            SqlQueryRepository commandHandlerRepository,
            ChildCalendarRepository childCalendarRepository,
            ChildMoneyRepository childMoneyRepository,
            ChildAccountRepository childAccountRepository,
            ChildRepository childRepository,
            ChildImageRepository childImageRepository,
            FamilyAccountRepository familyAccountRepository,
            FamilyRepository familyRepository,
            ParentFamilyAccountRepository parentFamilyAccountRepository,
            MoneyMovementRepository moneyMovementRepository,
            ChildAccountMoneyMovementCodeRepository childAccountMoneyMovementCodeRepository, MovementReasonCodeRepository movementActionCodeRepository,
            ToCoreMapper toCoreIdentity,
            ToInfraMapper toInfraMapper, AdapterHelper adapterHelper) {
        this.cmdRepository = commandHandlerRepository;
        this.childCalendarRepository = childCalendarRepository;
        this.childMoneyRepository = childMoneyRepository;
        this.childAccountRepository = childAccountRepository;
        this.childRepository = childRepository;
        this.childImageRepository = childImageRepository;
      this.familyAccountRepository = familyAccountRepository;
      this.familyRepository = familyRepository;
      this.parentFamilyAccountRepository = parentFamilyAccountRepository;
      this.moneyMovementRepository = moneyMovementRepository;
      this.childAccountMoneyMovementCodeRepository = childAccountMoneyMovementCodeRepository;
      this.movementReasonRepository = movementActionCodeRepository;
      this.toCoreIdentity = toCoreIdentity;
        this.toInfraMapper = toInfraMapper;
      this.adapterHelper = adapterHelper;
    }

    @Override
    public Mono<ChildMoneyAccount> loadActiveChildAccountAggregate(ChildMoneyAccountIdentity childMoneyAccountId) {

      // Date actuelle
      final var actualDate = LocalDate.now();

      // Periode d'argent de poche mensuelle
      LocalDate startMonthDate = adapterHelper.loadStartDate(actualDate, PeriodSubscription.MONTH);
      LocalDate endMonthDate = adapterHelper.loadEndDay(actualDate, PeriodSubscription.MONTH);

      // Periode d'argent de poche semaine
      LocalDate startWeekDate = adapterHelper.loadStartDate(actualDate, PeriodSubscription.WEEK);
      LocalDate endWeekDate = adapterHelper.loadEndDay(actualDate, PeriodSubscription.WEEK);

      var childAccountId = toInfraMapper.toTechnicalId(childMoneyAccountId);
      return loadActiveChildAccountCalendar(childAccountId, startMonthDate, endMonthDate)
              .switchIfEmpty(loadActiveChildAccountCalendar(childAccountId, startWeekDate, endWeekDate))
              .switchIfEmpty(Mono.error(new CalendarPeriodNotFound("Il n'y a pas de periode mensuelle ou hebdomadaire associée a ce compte d'argent de poche")))
              .flatMap(activeAccountCalendar ->
                      loadChildAccountInformation(childAccountId, activeAccountCalendar)
              );
    }

    @Override
    public Mono<FamilyAccount> loadFamilyAccountFromParent(ParentIdentity parent) {
        long parentId = Long.parseLong(parent.getIdentity());
        return cmdRepository
                .findFamilyAccount(parentId)
                .switchIfEmpty(Mono.error(new FamilyAccountForbiddenException("Aucun compte de famille n'est associé à votre compte")))
                .map(toCoreIdentity::toCoreFamilyAccount);
    }

    @Override
    public Mono<ChildMoneyAccountIdentity> updateActiveChildMoneyAccount(ChildMoneyAccount updatedChildAccount) {

        long childAccountId = toInfraMapper.toTechnicalId(updatedChildAccount.getChildMoneyAccountId());
        LocalDate startPeriodDate = updatedChildAccount.getCalendarSubscription().startDay();
        LocalDate endPeriodDate = updatedChildAccount.getCalendarSubscription().endDay();

        var chileEntityToUpdate = childRepository.findByChildAccountId(childAccountId);
        var childImageEntityToUpdate = chileEntityToUpdate.flatMap(childToUpdate -> childImageRepository.findById(childToUpdate.getChildImageId()));
        var childCalendarEntityToUpdate = childCalendarRepository.findFirstByChildAccountIdAndPeriodStartDayAndPeriodEndDay(childAccountId, startPeriodDate, endPeriodDate);
        var childMoneyEntityToUpdate = childCalendarEntityToUpdate.flatMap(childCalendarToUpdate -> childMoneyRepository.findByAccountCalendarId(childCalendarToUpdate.getId()));

        return Mono.zip(chileEntityToUpdate, childImageEntityToUpdate, childCalendarEntityToUpdate, childMoneyEntityToUpdate)
        .flatMap(dataTuple -> {
          var oldChildEntity = dataTuple.getT1();
          var oldChileImageEntity = dataTuple.getT2();
          var oldChildCalendarEntity = dataTuple.getT3();
          var oldChildMoneyEntity = dataTuple.getT4();

          var updatedImageName = updatedChildAccount.getChild().childImage().imageRandomName();
          var updatedChildName = updatedChildAccount.getChild().firstName();
          var updatedRemainingMoney = updatedChildAccount.getChildMoney().remainingMoney().remainingMoney();
          var updatedMoneyAtPeriodStart = updatedChildAccount.getChildMoney().childMoneyAtPeriodStart();
          var updateCalendarPeriod = updatedChildAccount.getCalendarSubscription().periodSubscription();

          oldChildEntity.setNickname(updatedChildName);
          oldChileImageEntity.setImageName(updatedImageName);
          oldChildCalendarEntity.setCalendarPeriod(toInfraMapper.toPeriodCalendar(updateCalendarPeriod));
          oldChildMoneyEntity.setRemainingMoney(updatedRemainingMoney);
          oldChildMoneyEntity.setMoneyAtPeriodStart(updatedMoneyAtPeriodStart);

          return childRepository.save(oldChildEntity)
                  .then(childImageRepository.save(oldChileImageEntity))
                  .then(childMoneyRepository.save(oldChildMoneyEntity))
                  .thenReturn(childCalendarRepository.save(oldChildCalendarEntity));

        })
        .thenReturn(toCoreIdentity.toChildAccountIdentity(childAccountId));
    }

    @Override
    public Mono<ChildMoneyAccountIdentity> addMoneyMovement(MoneyMovement moneyMovement, ChildMoneyAccountIdentity childAccountIdentity) {
      long childAccountId = toInfraMapper.toTechnicalId(childAccountIdentity);

      var movementReasonCode = moneyMovement.reason().getCode();
      return movementReasonRepository
              .findFirstByMovementCode(movementReasonCode)
              .flatMap(movementReason ->
                      childAccountMoneyMovementCodeRepository
                              .findFirstByMovementCodeIdAndChildAccountId(movementReason.getId(), childAccountId)
              )
              .flatMap(movementReasonByChildAccount -> {
                MoneyMovementEntity moneyMovementEntity = new MoneyMovementEntity();
                moneyMovementEntity.setChildAccountId(childAccountId);
                moneyMovementEntity.setAddByParentId(toInfraMapper.toTechnicalId(moneyMovement.initiatedByParent()));
                moneyMovementEntity.setChildAccountMovementCodeId(movementReasonByChildAccount.getId());
                moneyMovementEntity.setMovementAddAt(moneyMovement.occurredAt());
                moneyMovementEntity.setMovementActionCode(moneyMovement.action().getActionCode());

                return moneyMovementRepository.save(moneyMovementEntity)
                        .thenReturn(childAccountIdentity);
              });
    }

    @Override
    public Mono<ChildMoneyAccountIdentity> createChildMoneyAccount(ChildMoneyAccount childMoneyAccountToBeCreated, FamilyAccount familyAccount) {
        final boolean isChildAccountIdToIncludeInEntity = false;
        final BigDecimal defaultFluctuationPrice = BigDecimal.valueOf(0.5);

       ChildAccountEntity childAccountToSave = toInfraMapper.toChildAccountEntity(
               childMoneyAccountToBeCreated,
               familyAccount,
               isChildAccountIdToIncludeInEntity);

       //ChildAccountCalendarEntity calendar = toInfraMapper.toCalendarEntity(childMoneyAccountToBeCreated);
       ChildImageEntity childImageEntityToSave = toInfraMapper.toChildImageEntity(childMoneyAccountToBeCreated);

       return Mono.zip(
               childImageRepository.save(childImageEntityToSave),
               childAccountRepository.save(childAccountToSave)
       )
       .flatMap(tuple -> {
            var childImageEntitySaved = tuple.getT1();
            var childAccountEntitySaved = tuple.getT2();
            var childAccountId = childAccountEntitySaved.getId();

             ChildAccountCalendarEntity calendarEntityToSave =
                     toInfraMapper.toCalendarEntityToCreate(
                             childMoneyAccountToBeCreated,
                             childAccountId
                     );

             ChildEntity childToSave =
                     toInfraMapper.toChildEntityToCreate(
                             childMoneyAccountToBeCreated.getChild(),
                             childAccountId,
                             childImageEntitySaved.getId()
                     );

             var movementsSaved = movementReasonRepository
                     .findAll()
                     .collectList()
                     .flatMap(  movements -> {
                          List<ChildAccountMoneyMovementCodeEntity> childMovementReasonEntities = toInfraMapper
                                  .toChildMovementReasonEntities(childAccountId, defaultFluctuationPrice, movements);

                         return childAccountMoneyMovementCodeRepository
                                 .saveAll(childMovementReasonEntities)
                                 .then();
                     });

             return movementsSaved.then(
                     Mono.zip(
                     childRepository.save(childToSave),
                     childCalendarRepository.save(calendarEntityToSave)
             ).flatMap(tupleData -> {
                var calendarSaved = tupleData.getT2();

               ChildMoneyEntity childMoneyToCreate = toInfraMapper.toChildMoneyEntityToCreate(
                       childMoneyAccountToBeCreated,
                       childAccountId,
                       calendarSaved.getId());

               return childMoneyRepository.save(childMoneyToCreate);
             })).map(childAccountCreated -> toCoreIdentity.toChildAccountIdentity(childAccountCreated.getChildAccountId()));
       });
    }

    @Override
    public Mono<FamilyAccountIdentity> createFamilyAccount(FamilyAccount familyAccount, ParentIdentity parentCreatingAccount) {
      String familyName = familyAccount.getFamily().familyName();

      // Map vers familyAccountEntity
      FamilyAccountEntity familyAccountToRegister = toInfraMapper.toFamilyAccountToCreateEntity();

      return familyAccountRepository.save(familyAccountToRegister)
              .flatMap(familyAccountRegistered -> {
                var familyAccountId = familyAccountRegistered.getId();

                var familyToRegister = toInfraMapper.toFamilyToCreateEntity(familyAccountId, familyName);
                var parentFamilyAccountToLink = toInfraMapper.toParentFamilyAccountEntity(parentCreatingAccount, familyAccountId);

                return familyRepository.save(familyToRegister)
                        .then(parentFamilyAccountRepository.save(parentFamilyAccountToLink))
                        .thenReturn(familyAccountId);
              })
              .map(toCoreIdentity::toFamilyAccountIdentity);
    }

    @Override
    public Mono<Boolean> initializeNextPeriod() {


        LocalDate activateNextPeriodDate = LocalDate.now();
        return childAccountRepository.findAll()
                .flatMap(childAccount ->
                        childCalendarRepository.findFirstByChildAccountIdOrderByPeriodStartDayDesc(childAccount.getId())
                                .flatMap(accountCalendar -> {
                                  LOGGER.debug(childAccount.getId());
                                    var startDay = accountCalendar.getPeriodStartDay();
                                    var endDay = accountCalendar.getPeriodEndDay();
                                    var childAccountIdentity = toCoreIdentity.toChildAccountIdentity(childAccount.getId());
                                   return this.childAccountAggregate(childAccountIdentity, startDay, endDay)
                                       .flatMap(childAccountAggregate -> {
                                         var nextPeriodAggregate = childAccountAggregate.initializeNextPeriod(activateNextPeriodDate);
                                         return childCalendarRepository.save(toInfraMapper.toCalendarEntity(nextPeriodAggregate))
                                                 .flatMap(nextPeriodCalendar ->
                                                        childMoneyRepository.save(toInfraMapper.toChildMoneyEntity(nextPeriodAggregate, nextPeriodCalendar.getId()))
                                                 );
                                          }
                                       )
                                      .then();
                                  })
                )
                .then(Mono.just(true));
    }

    @Override
    public Mono<AddMoneyMovementReason> loadMoneyMovementReasonByCode(String movementReasonCode, ChildMoneyAccountIdentity childAccountIdentity) {
      var childAccountId = toInfraMapper.toTechnicalId(childAccountIdentity);
      var reasonMovementIdentity = movementReasonRepository.findFirstByMovementCode(movementReasonCode);

      var movementReasonByChildAccountIdentity = reasonMovementIdentity.flatMap(reasonMovement ->
              childAccountMoneyMovementCodeRepository.findFirstByMovementCodeIdAndChildAccountId(reasonMovement.getId(), childAccountId)
      );

    return Mono.zip(reasonMovementIdentity, movementReasonByChildAccountIdentity)
            .map(tupleData -> {
                var reasonMovement = tupleData.getT1();
                var movementReasonByChildAccount =  tupleData.getT2();

                return new AddMoneyMovementReasonDto(
                        movementReasonByChildAccount.getMovementFluctuationPrice(),
                        reasonMovement.getMovementCode(),
                        reasonMovement.getMovementName()
                );
            });
  }

  /**
   * Recherche de la periode du compte d'argent de poche qui est actif
   * Pour rappel, la periode de l'argrnt de poche peut être mensuelle ou hebdomadaire
   *
   * @return ChildAccountCalendarEntity
   */
  private Mono<ChildAccountCalendarEntity> loadActiveChildAccountCalendar(long childAccountId, LocalDate startDay, LocalDate endDay) {
    return childCalendarRepository.findFirstByChildAccountIdAndPeriodStartDayAndPeriodEndDay(childAccountId, startDay, endDay);
  }

  /**
   * Chargement du compte d'argent de poche en fonction  de la periode active
   *
   * @param childAccountId L'identifiant du compte d'argent de poche
   * @param activeAccountCalendar La periode active de l'argent de poche
   *
   * @see #loadActiveChildAccountCalendar(long, LocalDate, LocalDate)
   *
   * @return ChildMoneyAccount
   */
  private Mono<ChildMoneyAccount> loadChildAccountInformation(long childAccountId, ChildAccountCalendarEntity activeAccountCalendar) {
    var childEntity = childRepository.findByChildAccountId(childAccountId);
    var childImageEntity = childEntity.flatMap(child -> childImageRepository.findById(child.getChildImageId()));
    var accountMoneyEntity = childMoneyRepository.findByAccountCalendarId(activeAccountCalendar.getId());

    return Mono.zip(childEntity, childImageEntity, accountMoneyEntity)
            .map(tupleData -> {
                var child = tupleData.getT1();
                var childImage = tupleData.getT2();
                var accountMoney = tupleData.getT3();

                return toCoreIdentity.toChildMoneyAccount(
                    childAccountId,
                    child,
                    childImage,
                    activeAccountCalendar,
                    accountMoney);
            });
  }

  private Mono<ChildMoneyAccount> childAccountAggregate(
          ChildMoneyAccountIdentity childMoneyAccountId,
          LocalDate startMonthDate,
          LocalDate endMonthDate) {

    var childAccountId = toInfraMapper.toTechnicalId(childMoneyAccountId);
    return loadActiveChildAccountCalendar(childAccountId, startMonthDate, endMonthDate)
            .switchIfEmpty(Mono.error(new CalendarPeriodNotFound("Il n'y a pas de periode mensuelle ou hebdomadaire associée a ce compte d'argent de poche")))
            .flatMap(activeAccountCalendar ->
                    loadChildAccountInformation(childAccountId, activeAccountCalendar)
            );
  }
}
