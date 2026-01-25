package com.ctoutweb.argenDePoche.infra.adapter.secondaryAdapter;

import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToCoreMapper;
import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToInfraMapper;
import com.ctoutweb.argenDePoche.infra.repository.*;
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

  public QueryRepositoryAdapter(
          SqlQueryRepository sqlQueryRepository,
          ParentFamilyAccountRepository parentFamilyAccountRepository,
          ToInfraMapper toInfraMapper,
          ToCoreMapper toCoreMapper, ChildAccountRepository childAccountRepository, ChildRepository childRepository, ChildImageRepository childImageRepository, FamilyRepository familyRepository) {
    this.sqlQueryRepository = sqlQueryRepository;
    this.parentFamilyAccountRepository = parentFamilyAccountRepository;
    this.toInfraMapper = toInfraMapper;
    this.toCoreMapper = toCoreMapper;
    this.childAccountRepository = childAccountRepository;
    this.childRepository = childRepository;
    this.childImageRepository = childImageRepository;
    this.familyRepository = familyRepository;
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
        return sqlQueryRepository
                .loadChildAccount(childAccountId)
                .map(dto -> toCoreMapper.toChildAccountDto(dto));
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
