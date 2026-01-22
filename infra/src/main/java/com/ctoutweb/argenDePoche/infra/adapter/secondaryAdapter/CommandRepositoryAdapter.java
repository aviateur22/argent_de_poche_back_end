package com.ctoutweb.argenDePoche.infra.adapter.secondaryAdapter;

import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToCoreMapper;
import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToInfraMapper;
import com.ctoutweb.argenDePoche.infra.repository.*;
import com.ctoutweb.argenDePoche.infra.repository.entity.*;
import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccount;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccount;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Component
public class CommandRepositoryAdapter implements CommandRepository {
    private final CmdRepository cmdRepository;
    private final ChildCalendarRepository childCalendarRepository;
    private final ChildMoneyRepository childMoneyRepository;
    private final ChildAccountRepository childAccountRepository;
    private final ChildRepository childRepository;
    private final ChildImageRepository childImageRepository;



    private final ToCoreMapper toCoreIdentity;
    private final ToInfraMapper toInfraMapper;

    public CommandRepositoryAdapter(
            CmdRepository commandHandlerRepository,
            ChildCalendarRepository childCalendarRepository,
            ChildMoneyRepository childMoneyRepository,
            ChildAccountRepository childAccountRepository,
            ChildRepository childRepository, ChildImageRepository childImageRepository,
            ToCoreMapper toCoreIdentity,
            ToInfraMapper toInfraMapper) {
        this.cmdRepository = commandHandlerRepository;
        this.childCalendarRepository = childCalendarRepository;
        this.childMoneyRepository = childMoneyRepository;
        this.childAccountRepository = childAccountRepository;
        this.childRepository = childRepository;
        this.childImageRepository = childImageRepository;
        this.toCoreIdentity = toCoreIdentity;
        this.toInfraMapper = toInfraMapper;
    }

    @Override
    public Mono<ChildMoneyAccount> loadChildMoneyAccountAggregate(ChildMoneyAccountIdentity childMoneyAccountId, LocalDate periodStart, LocalDate periodEnd) {
        return null;
    }

    @Override
    public Mono<FamilyAccount> loadFamilyAccountFromParent(ParentIdentity parent) {
        long parentId = Long.parseLong(parent.getIdentity());
        return cmdRepository
                .findFamilyAccount(parentId)
                .map(toCoreIdentity::toCoreFamilyAccount);
    }

    @Override
    public Mono<ChildMoneyAccountIdentity> updateChildMoneyAccount(ChildMoneyAccount updatedChildAccount) {
        return null;
    }

    @Override
    public Mono<ChildMoneyAccountIdentity> createChildMoneyAccount(ChildMoneyAccount childMoneyAccountToBeCreated, FamilyAccount familyAccount) {
        final boolean isChildAccountIdToIncludeInEntity = false;
        final boolean isChildIdToIncludeInEntity = false;

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
    public Mono<FamilyAccountIdentity> createFamilyAccount(FamilyAccount familyAccount) {
        return null;
    }
}
