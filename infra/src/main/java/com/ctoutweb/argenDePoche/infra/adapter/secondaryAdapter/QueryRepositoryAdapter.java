package com.ctoutweb.argenDePoche.infra.adapter.secondaryAdapter;

import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToCoreMapper;
import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToInfraMapper;
import com.ctoutweb.argenDePoche.infra.repository.*;
import com.ctoutweb.argentDePoche.application.exception.ChildCalendarNotFoundException;
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

@Component
public class QueryRepositoryAdapter implements QueryRepository {

    private final SqlQueryRepository sqlQueryRepository;
    private final ToInfraMapper toInfraMapper;
    private final ToCoreMapper toCoreMapper;
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
          ToCoreMapper toCoreMapper, ChildAccountRepository childAccountRepository, ChildRepository childRepository, ChildImageRepository childImageRepository, FamilyRepository familyRepository, ChildCalendarRepository childCalendarRepository, ChildMoneyRepository childMoneyRepository) {
    this.sqlQueryRepository = sqlQueryRepository;
    this.parentFamilyAccountRepository = parentFamilyAccountRepository;
    this.toInfraMapper = toInfraMapper;
    this.toCoreMapper = toCoreMapper;
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
    public Mono<ChildAccountDto> loadChildMoneyAccount(ChildMoneyAccountIdentity childMoneyAccountId) {
        long childAccountId = toInfraMapper.toTechnicalId(childMoneyAccountId);

        return childRepository.findByChildAccountId(childAccountId)
                .switchIfEmpty(Mono.error(new ChildNotFindException("L'enfant associé au compte d'argent de poche n'est pas trouvé")))
                .flatMap(child -> childImageRepository.findById(child.getChildImageId())
                        .switchIfEmpty(Mono.error(new ChildImageNotFindException("L'image associé au compte d'argent de poche n'est pas trouvé")))
                        .flatMap( childImage ->
                                childCalendarRepository.findFirstByChildAccountIdOrderByPeriodStartDayDesc(childAccountId)
                                        .switchIfEmpty(Mono.error(new ChildCalendarNotFoundException("Le calendrier associé au compte d'argent de poche n'est pas trouvé")))
                                        .flatMap(calendar ->
                                                childMoneyRepository.findByAccountCalendarId(calendar.getId())
                                                        .switchIfEmpty(Mono.error(new ChildMoneyNotFoundException("L'argent associé au compte d'argent de poche n'est pas trouvé")))
                                                        .map(childMoney -> {
                                                          var childId = child.getId();
                                                          var childName = child.getNickname();
                                                          var imageName = childImage.getImageName();
                                                          var moneyAtPeriodStart = childMoney.getMoneyAtPeriodStart();
                                                          var moneyRemaining = childMoney.getRemainingMoney();
                                                          var actualDate = LocalDate.now();
                                                          var calendarStartDate = calendar.getPeriodStartDay();
                                                          var calendarEndDate = calendar.getPeriodEndDay();
                                                          var periodSubscription = calendar.getCalendarPeriod();

                                                          return toCoreMapper.toChildAccountDto(
                                                                  childAccountId,
                                                                  childId,
                                                                  childName,
                                                                  imageName,
                                                                  moneyAtPeriodStart,
                                                                  moneyRemaining,
                                                                  actualDate,
                                                                  calendarStartDate,
                                                                  calendarEndDate,
                                                                  periodSubscription
                                                                  );
                                                        })
                                        )

                        )
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
}
