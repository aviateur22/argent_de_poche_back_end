package com.ctoutweb.argenDePoche.infra.adapter.secondaryAdapter;

import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToCoreMapper;
import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToInfraMapper;
import com.ctoutweb.argenDePoche.infra.repository.*;
import com.ctoutweb.argenDePoche.infra.repository.entity.*;
import com.ctoutweb.argentDePoche.application.exception.FamilyAccountForbiddenException;
import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccount;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccount;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

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

    private final ToCoreMapper toCoreIdentity;
    private final ToInfraMapper toInfraMapper;

    public CommandRepositoryAdapter(
            SqlQueryRepository commandHandlerRepository,
            ChildCalendarRepository childCalendarRepository,
            ChildMoneyRepository childMoneyRepository,
            ChildAccountRepository childAccountRepository,
            ChildRepository childRepository,
            ChildImageRepository childImageRepository,
            FamilyAccountRepository familyAccountRepository,
            FamilyRepository familyRepository,
            ParentFamilyAccountRepository parentFamilyAccountRepository, MoneyMovementRepository moneyMovementRepository,
            ToCoreMapper toCoreIdentity,
            ToInfraMapper toInfraMapper) {
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
      this.toCoreIdentity = toCoreIdentity;
        this.toInfraMapper = toInfraMapper;
    }

    @Override
    public Mono<ChildMoneyAccount> loadChildMoneyAccountAggregate(ChildMoneyAccountIdentity childMoneyAccountId, LocalDate periodStart, LocalDate periodEnd) {
        var childAccountId = toInfraMapper.toTechnicalId(childMoneyAccountId);
        return childRepository.findByChildAccountId(childAccountId)
                .flatMap(child ->
                        childImageRepository.findById(child.getChildImageId())
                                .flatMap(childImage ->
                                        childCalendarRepository.findFirstByChildAccountIdOrderByPeriodStartDayDesc(childAccountId)
                                                .flatMap(accountCalendar ->
                                                        childMoneyRepository.findByAccountCalendarId(accountCalendar.getId())
                                                                .flatMap(accountMoney ->
                                                                        moneyMovementRepository.findByChildAccountIdAndDateRange(
                                                                                childAccountId,
                                                                                accountCalendar.getPeriodStartDay(),
                                                                                accountCalendar.getPeriodEndDay()
                                                                        )
                                                                        .collectList()
                                                                        .map( moneyMovements -> {
                                                                                var childIdentity = toCoreIdentity.toChild(child, childImage);
                                                                                var subscriptionCalendar = toCoreIdentity.toSubscriptionCalendar(accountCalendar, LocalDate.now());
                                                                                var childMoney = toCoreIdentity.toChildMoneyAccount(accountMoney);

                                                                                return new ChildMoneyAccount(
                                                                                  toCoreIdentity.toChildAccountIdentity(childAccountId),
                                                                                  childIdentity,
                                                                                  subscriptionCalendar,
                                                                                  childMoney
                                                                                );

                                                                        })

                                                                )


                                                )


                                        )
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
    public Mono<ChildMoneyAccountIdentity> updateChildMoneyAccount(ChildMoneyAccount updatedChildAccount) {
        long childAccountId = toInfraMapper.toTechnicalId(updatedChildAccount.getChildMoneyAccountId());
        return childRepository.findByChildAccountId(childAccountId)
                .flatMap(childEntity ->
                        childImageRepository.findById(childEntity.getId())
                                .flatMap( childImageEntity ->
                                        childCalendarRepository.findFirstByChildAccountIdOrderByPeriodStartDayDesc(childAccountId)
                                                .flatMap(childCalendarEntity ->
                                                        childMoneyRepository.findByAccountCalendarId(childCalendarEntity.getId())
                                                                .flatMap(childMoneyAccountEntity -> {
                                                                  // Mise a jour des données
                                                                  var updatedImageName = updatedChildAccount.getChild().childImage().imageRandomName();
                                                                  var childName = updatedChildAccount.getChild().firstName();
                                                                  var remainingMoney = updatedChildAccount.getChildMoney().remainingMoney().remainingMoney();
                                                                  var moneyAtPeriodStart = updatedChildAccount.getChildMoney().childMoneyAtPeriodStart();
                                                                  var calendarPeriod = updatedChildAccount.getCalendarSubscription().periodSubscription();

                                                                  childEntity.setNickname(childName);
                                                                  childImageEntity.setImageName(updatedImageName);
                                                                  childMoneyAccountEntity.setRemainingMoney(remainingMoney);
                                                                  childMoneyAccountEntity.setMoneyAtPeriodStart(moneyAtPeriodStart);
                                                                  childCalendarEntity.setCalendarPeriod(toInfraMapper.toPeriodCalendar(calendarPeriod));

                                                                  return childRepository.save(childEntity)
                                                                          .then(childImageRepository.save(childImageEntity))
                                                                          .then(childMoneyRepository.save(childMoneyAccountEntity))
                                                                          .then(childCalendarRepository.save(childCalendarEntity))
                                                                          .thenReturn(toCoreIdentity.toChildAccountIdentity(childAccountId));
                                                                })
                                                )

                                ));



    }

    @Override
    public Mono<ChildMoneyAccountIdentity> createChildMoneyAccount(ChildMoneyAccount childMoneyAccountToBeCreated, FamilyAccount familyAccount) {
        final boolean isChildAccountIdToIncludeInEntity = false;

       ChildAccountEntity childAccountToCreate = toInfraMapper.toChildAccountEntity(childMoneyAccountToBeCreated, familyAccount, isChildAccountIdToIncludeInEntity);
       ChildAccountCalendarEntity calendar = toInfraMapper.toCalendarEntity(childMoneyAccountToBeCreated);
        ChildImageEntity childImage = toInfraMapper.toChildImageEntity(childMoneyAccountToBeCreated);

       return childImageRepository.save(childImage)
           .flatMap(childImageSaved ->
                childAccountRepository.save(childAccountToCreate)
                    .flatMap(createdChildAccount -> {
                        var childAccountIdCreated = createdChildAccount.getId();
                        ChildAccountCalendarEntity calendarToCreate = toInfraMapper.toCalendarEntityToCreate(childMoneyAccountToBeCreated, childAccountIdCreated);
                        ChildMoneyEntity childMoneyToCreate = toInfraMapper.toChildMoneyEntityToCreate(childMoneyAccountToBeCreated, childAccountIdCreated);
                        ChildEntity childToCreate = toInfraMapper.toChildEntityToCreate(childMoneyAccountToBeCreated.getChild(), childAccountIdCreated, childImageSaved.getId());

                        return childMoneyRepository.save(childMoneyToCreate)
                        .then(childRepository.save(childToCreate))
                        .then(childCalendarRepository.save(calendarToCreate))
                        .thenReturn(createdChildAccount);
                    })
           ).map(childAccountCreated -> toCoreIdentity.toChildAccountIdentity(childAccountCreated.getId()));

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
                                 return this.loadChildMoneyAccountAggregate(toCoreIdentity.toChildAccountIdentity(childAccount.getId()), startDay, endDay)
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
}
