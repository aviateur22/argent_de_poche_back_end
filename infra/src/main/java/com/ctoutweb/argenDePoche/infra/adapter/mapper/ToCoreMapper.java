package com.ctoutweb.argenDePoche.infra.adapter.mapper;

import com.ctoutweb.argenDePoche.infra.repository.dto.ChildAccountProjection;
import com.ctoutweb.argenDePoche.infra.repository.dto.FamilyAccountProjection;
import com.ctoutweb.argenDePoche.infra.repository.dto.LoadFamilyAccountProjection;
import com.ctoutweb.argenDePoche.infra.repository.entity.ChildAccountEntity;
import com.ctoutweb.argenDePoche.infra.repository.entity.FamilyEntity;
import com.ctoutweb.argentDePoche.application.port.NextChildAccountIdentities;
import com.ctoutweb.argentDePoche.application.port.NextFamilyAccountIdentities;
import com.ctoutweb.argentDePoche.application.query.dto.ChildAccountDto;
import com.ctoutweb.argentDePoche.application.query.dto.FamilyChildDto;
import com.ctoutweb.argentDePoche.application.query.dto.FamilyDto;
import com.ctoutweb.argentDePoche.application.query.dto.FamilyInformationDto;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.child.ChildIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccount;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.family.Family;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.family.FamilyIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class ToCoreMapper {

    /**
     * Map vers l'identifiant d'un parent
     *
     * @param technicalId L'identification technique du parent
     *
     * @return L'identifiant du parent pour le domaine
     */
    public ParentIdentity toParentIdentity(long technicalId) {
        return new ParentIdentity(technicalId);
    }

    /**
     * Map vers l'identifiant d'un enfant
     *
     * @param childId L'identification technique de l'enfant
     *
     * @return L'identifiant de l'enfant pour le domaine
     */
    public ChildIdentity toChildIdentity(long childId) {
        return new ChildIdentity(childId);
    }

    /**
     * Map vers l'identifiant d'un compte d'argent de poche
     *
     * @param childAccountId L'identification technique du compte d'argent de poche
     *
     * @return L'identifiant du compte d'argent de poche pour le domaine
     */
    public ChildMoneyAccountIdentity toChildAccountIdentity(long childAccountId) {
        return new ChildMoneyAccountIdentity(childAccountId);
    }

    /**
     * Map vers l'aggregat FamilyAccount
     *
     * @param projection Les données issue de la base de données
     *
     * @return L'aggégat
     */
    public FamilyAccount toCoreFamilyAccount(FamilyAccountProjection projection) {
       return new FamilyAccount(
                    mapToFamilyAccountIdentity(projection.familyAccountId()),
                    new Family(
                            mapToFamilyIdentity(projection.familyAccountId()),
                            projection.familyName()
                    ),
                    projection.parentIds()
                            .stream()
                            .map(this::mapToParentIdentity)
                            .toList(),
                   Optional.ofNullable(projection.childAccountIds())
                           .orElse(List.of())
                            .stream()
                            .map(this::mapToChildMoneyAccountIdentity)
                            .toList()
                );
    }

    /**
     * Map les identifiants techniques générés vers le domaine
     *
     * @param nextChildAccountId L'identifiant technique généré pour le compte d'argent de poche
     * @param nextChildId L'identifiant tecgnique généré pour l'enfant
     *
     * @return Les identifiants technique mappé pour le domaine
     */
    public NextChildAccountIdentities toNextChildAccountIdentities(long nextChildAccountId, long nextChildId) {
        return new NextChildAccountIdentities() {
            @Override
            public ChildIdentity getNextChildIdentity() {
                return toChildIdentity(nextChildId);
            }

            @Override
            public ChildMoneyAccountIdentity getNextChildMoneyAccountId() {
                return toChildAccountIdentity(nextChildAccountId);
            }
        };
    }

    /**
     * Map un objet contenant les identifiants techniqye générés de facons aléatoire pour la création d'un compte familiale:
     * - nextFamilyAccountId
     * - nextFamilyId
     * vers le Domain:
     *   - FamilyAccountIdentity
     *   - FamilyIdentity
     *
     * @param nextFamilyAccountId Identifiant technique a mapper vers FamilyAccountIdentity
     * @param nextFamilyId Identifiant technique à mapper vers FamilyIdentity
     *
     * @return
     */
    public NextFamilyAccountIdentities toNextFamilyIdentities(long nextFamilyAccountId, long nextFamilyId) {
        return new NextFamilyAccountIdentities() {
            @Override
            public FamilyAccountIdentity getNextFamilyAccountIdentity() {
                return mapToFamilyAccountIdentity(nextFamilyAccountId);
            }

            @Override
            public FamilyIdentity getNexFamilyIdentity() {
                return mapToFamilyIdentity(nextFamilyId);
            }
        };
    }

    /**
     * Renvoie une Identity de type FamilyAccountIdentity
     *
     * @param familyAccountId L'identifiant technqie du compte de famille
     *
     * @return FamilyAccountIdentity
     */
    public FamilyAccountIdentity toFamilyAccountIdentity(long familyAccountId) {
        return mapToFamilyAccountIdentity(familyAccountId);
    }

    /**
     * Renvoie un objet de type ChildAccountDto
     *
     * @param dto Données issue de la base contenant les donnes du compte
     *
     * @return ChildAccountDto
     */
    public ChildAccountDto toChildAccountDto(ChildAccountProjection dto) {
        return new ChildAccountDto(
                toChildAccountIdentity(dto.childMoneyAccountId()),
                dto.imageRandomName(),
                toChildIdentity(dto.childId()),
                dto.childName(),
                dto.remainingMoney(),
                dto.moneyAtPeriodStart(),
                dto.periodSubscriptionName(),
                LocalDate.now(),
                dto.startPeriodDate(),
                dto.endPeriodDate()
        );
    }

    /**
     * Map l'entity ChildAccountEntity vers l'objet métier FamilyChildDto
     *
     * @param childAccountId Entité ChildAccountEntity issue de la bas de données
     *
     * @return une instance de FamilyChildDto
     */
    public FamilyChildDto toFamilyChildDto(long childAccountId, String childName, String childImageName) {
        return new FamilyChildDto(
                toChildAccountIdentity(childAccountId),
                childName,
                childImageName
        );
    }

    /**
     * Map l'entity FamilyEntity vers l'objet FamilyInformationDto
     *
     * @param family - Les données de la famille issue de la base de donnée
     *
     * @return Une instance FamilyInformationDto
     */
    public FamilyInformationDto toFamilyInformationDto(FamilyEntity family) {
        return new FamilyInformationDto(toFamilyAccountIdentity(family.getFamilyAccountId()), family.getName());
    }

    /**
     *
     * @param data
     * @return
     */
    public FamilyDto toFamilyDto(LoadFamilyAccountProjection data) {
        return new FamilyDto(
                toFamilyAccountIdentity(data.familyAccountId()),
                data.familyName(),
                List.of()
        );
    }

    private FamilyAccountIdentity mapToFamilyAccountIdentity(long familyAccountId) {
        return new FamilyAccountIdentity(familyAccountId);
    }

    private FamilyIdentity mapToFamilyIdentity(long familyId) {
        return new FamilyIdentity(familyId);
    }

    private ParentIdentity mapToParentIdentity(long parentId) {
        return new ParentIdentity(parentId);
    }

    private ChildMoneyAccountIdentity mapToChildMoneyAccountIdentity(long childAccountId) {
        return new ChildMoneyAccountIdentity(childAccountId);
    }
}
