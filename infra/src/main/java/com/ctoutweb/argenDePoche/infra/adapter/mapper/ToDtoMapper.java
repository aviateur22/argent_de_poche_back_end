package com.ctoutweb.argenDePoche.infra.adapter.mapper;

import com.ctoutweb.argenDePoche.infra.model.dto.controller.ChildAccountResponseDto;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.CreateChildAccountResponseDto;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.CreateFamilyAccountResponseDto;
import com.ctoutweb.argenDePoche.infra.model.dto.UpdatedChildImageDto;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.UpdatedChildAccountResponseDto;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.familyAccountResponse.ChildDto;
import com.ctoutweb.argenDePoche.infra.model.dto.controller.familyAccountResponse.FamilyAccountResponseDto;
import com.ctoutweb.argentDePoche.application.command.dto.UpdatedChildImage;
import com.ctoutweb.argentDePoche.application.query.dto.ChildAccountDto;
import com.ctoutweb.argentDePoche.application.query.dto.FamilyChildDto;
import com.ctoutweb.argentDePoche.application.query.dto.FamilyDto;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import org.springframework.stereotype.Component;

/**
 * Mapper utilisé uniquement pour mapper les données du layer Domaine / Core vers:
 * - des Dto du Layer Infra
 */
@Component
public class ToDtoMapper {

    private final ToInfraMapper infraMapper;

    public ToDtoMapper(ToInfraMapper infraMapper) {
        this.infraMapper = infraMapper;
    }

    /**
     * Map vers CreateChildAccountResponseDto
     *
     * @param createdChildAccountId L'identitifiant technique du compte d'argent de poche créé
     *
     * @return CreateChildAccountResponseDto
     */
    public CreateChildAccountResponseDto mapToCreateChildAccountResponseDto(long createdChildAccountId) {
        return new CreateChildAccountResponseDto(createdChildAccountId);
    }

    /**
     * Map vers CreateFamilyAccountResponseDto
     *
     * @param familyTechnicalId L'identifiant technique de la famille qui est créée
     * @param parentCreatingAccountId L'identifiant technique du parent créant le compte
     *
     * @return CreateFamilyAccountResponseDto
     */
    public CreateFamilyAccountResponseDto mapToCreateFamilyAccountResponseDto(Long familyTechnicalId, long parentCreatingAccountId) {
        return new CreateFamilyAccountResponseDto(familyTechnicalId, parentCreatingAccountId);
    }


    /**
     * Renvoie un DTO de type ChildAccountResponseDto
     *
     * @param childAccountDto Compte d'argent provenant du Core et qui est a mapper
     *
     * @return ChildAccountResponseDto
     */
    public ChildAccountResponseDto toChildAccountResponseDto(ChildAccountDto childAccountDto) {
        return new ChildAccountResponseDto(
                infraMapper.toTechnicalId(childAccountDto.childAccountIdentity()),
                infraMapper.toTechnicalId(childAccountDto.childIdentity()),
                childAccountDto.imageRandomName(),
                childAccountDto.childName(),
                childAccountDto.remainingMoney(),
                childAccountDto.moneyAtPeriodStart(),
                childAccountDto.periodName(),
                childAccountDto.actualDate(),
                childAccountDto.startPeriodDate(),
                childAccountDto.endPeriodDate()
        );
    }

    /**
     * Renvoie un DTO de type FamilyAccountResponseDto
     *
     * @param familyDto Données du compte de la famille qui est a mapper vers FamilyAccountResponseDto
     *
     * @return une instance de FamilyAccountResponseDto
     */
    public FamilyAccountResponseDto mapToFamilyAccountResponseDto(FamilyDto familyDto) {

        var childAccounts = familyDto.familyChildrenDtos().stream().map(this::toChildDto).toList();

        return new FamilyAccountResponseDto(
                infraMapper.toTechnicalId(familyDto.familyAccountIdentity()),
                familyDto.familyName(),
                childAccounts
        );
    }

    /**
     * Map les données du useCase vers UpdatedChildImageDto.
     * Ces données renvoyées contiennent le nom random de la nouvelle image ainsi que l'ancien nom de l'image utilisé
     * Cela permettra de transféré la nouvelle image avec son nouveau nom et de supprimer l'ancienne
     *
     * @param updatedChildImage Les données de la mise a jour de l'image du useCase
     *
     * @return UpdatedChildImageDto
     */
    public UpdatedChildImageDto toUpdatedChildImageDto(UpdatedChildImage updatedChildImage) {

        return new UpdatedChildImageDto(
                infraMapper.toTechnicalId(updatedChildImage.childMoneyAccountIdentity()),
                updatedChildImage.newRandomImageName(),
                updatedChildImage.oldImageName()
        );
    }

    /**
     *
     * @param childMoneyAccountIdentity
     * @return
     */
    public UpdatedChildAccountResponseDto toUpdatedChildResponseDto(ChildMoneyAccountIdentity childMoneyAccountIdentity) {
        var childAccountId = infraMapper.toTechnicalId(childMoneyAccountIdentity);
        return new UpdatedChildAccountResponseDto(childAccountId);
    }

    /**
     * Renvoie un DTO de type ChildDto qui est requis pour l'objet FamilyAccountResponseDto
     *
     * @see FamilyAccountResponseDto
     *
     * @param childDto Les données sur les comptes d'argent des enfants
     *
     * @return Une instance de ChildDto
     */
    private ChildDto toChildDto(FamilyChildDto childDto) {
        return new ChildDto(
                infraMapper.toTechnicalId(childDto.childMoneyAccountIdentity()),
                childDto.name(),
                childDto.imageRandomName()
        );
    }

}
