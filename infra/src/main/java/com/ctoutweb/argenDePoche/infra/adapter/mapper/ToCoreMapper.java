package com.ctoutweb.argenDePoche.infra.adapter.mapper;

import com.ctoutweb.argenDePoche.infra.repository.dto.FamilyAccountProjection;
import com.ctoutweb.argenDePoche.infra.repository.entity.*;
import com.ctoutweb.argentDePoche.application.exception.ChildImageExtensionInvalidExtension;
import com.ctoutweb.argentDePoche.application.port.NextChildAccountIdentities;
import com.ctoutweb.argentDePoche.application.port.NextFamilyAccountIdentities;
import com.ctoutweb.argentDePoche.application.query.dto.ChildAccountDto;
import com.ctoutweb.argentDePoche.application.query.dto.FamilyChildDto;
import com.ctoutweb.argentDePoche.application.query.dto.FamilyInformationDto;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccount;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.child.Child;
import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.child.ChildIdentity;
import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.childImage.ChildImage;
import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.childImage.ImageExtension;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account.ChildMoney;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.calendar.PeriodSubscription;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.calendar.SubscriptionCalendar;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.remainingMoney.Devise;
import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.remainingMoney.RemainingMoney;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccount;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.family.Family;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.family.FamilyIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Mapper utilisée uniquement pour mapper les données du layer Infra vers:
 * - Layer Application
 * - Layer Core
 */
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

    public ImageExtension toImageExtension(String extension) {
        return switch (extension.toLowerCase()) {
            case "png" -> ImageExtension.PNG;
            case "svg" -> ImageExtension.SVG;
            case "jpeg" -> ImageExtension.JPEG;
            case "jpg" -> ImageExtension.JPEG;
            default -> throw new ChildImageExtensionInvalidExtension("L'exyension de l'image n'est pas valide");
        };
    }

    /**
     * Renvoie un objet de type ChildAccountDto
     *
     * @return ChildAccountDto
     */
    public ChildAccountDto toChildAccountDto(
            Long childAccountId,
            Long childId,
            String childName,
            String imageName,
            BigDecimal moneyAtPeriodStart,
            BigDecimal moneyRemaining,
            LocalDate actualDate,
            LocalDate calendarStartDate,
            LocalDate calendarEndDate,
            String periodSubscription
    ) {
        return new ChildAccountDto(
                toChildAccountIdentity(childAccountId),
                toChildIdentity(childId),
                childName,
                imageName,
                moneyAtPeriodStart,
                moneyRemaining,
                actualDate,
                calendarStartDate,
                calendarEndDate,
                periodSubscription
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
     * Map les données de la base vers L'Entity Child de l'aggregat ChildAccountAggregate
     *
     * @param childEntity Les données de l'enfant
     * @param childImageEntity Les données de l'image de l'enfant
     *
     * @return L'Entity Child de l'aggregat
     */
    public Child toChild(ChildEntity childEntity, ChildImageEntity childImageEntity) {
        ImageExtension childImageExtension = this.toImageExtension(childImageEntity.getExtension());
        var childImage = new ChildImage(childImageEntity.getImageName(), childImageExtension);
        return new Child(
                toChildIdentity(childEntity.getId()),
                childEntity.getNickname(),
                childImage);
    }

    /**
     * Map vers le valueObject SubscriptionCalendar de l'aggregat ChildAccountAggregate
     *
     * @param childAccountCalendarEntity Les données issues de la base
     * @param actualDate La date actuel de consulatation
     *
     * @return Le valueObject SubscriptionCalendar
     */
    public SubscriptionCalendar toSubscriptionCalendar(ChildAccountCalendarEntity childAccountCalendarEntity, LocalDate actualDate) {
        PeriodSubscription periodSubscription = PeriodSubscription.findPeriodSubscription(childAccountCalendarEntity.getCalendarPeriod());
        LocalDate startDay = childAccountCalendarEntity.getPeriodStartDay();
        LocalDate endDay = childAccountCalendarEntity.getPeriodEndDay();

        return new SubscriptionCalendar(
                actualDate, periodSubscription, startDay, endDay);
    }

    /**
     * Map vers le valueObject ChildMoney de l'aggregat ChildAccountAggregate
     *
     * @param childMoneyEntity Les données d'argent issue dela base
     *
     * @return Le valueObject ChildMoney
     */
    public ChildMoney toChildMoneyAccount(ChildMoneyEntity childMoneyEntity) {
        RemainingMoney remainingMoneyObjectValue = new RemainingMoney(
                childMoneyEntity.getRemainingMoney(),
                Devise.EUR
        );

        return new ChildMoney(
                childMoneyEntity.getMoneyAtPeriodStart(),
                remainingMoneyObjectValue
        );
    }

    /**
     * Renvoie l'aggregat du compte d'argent de poche
     *
     * @param childAccountId L'identifiant de l'aggregat
     * @param child L'enfant
     * @param childImage L'image de l'enfant
     * @param activeAccountCalendar La periode d'argent de poche
     * @param accountMoney L'argent du compte
     *
     * @return ChildMoneyAccount
     */
    public ChildMoneyAccount toChildMoneyAccount(
            long childAccountId,
            ChildEntity child,
            ChildImageEntity childImage,
            ChildAccountCalendarEntity activeAccountCalendar,
            ChildMoneyEntity accountMoney) {
        var childIdentity = toChild(child, childImage);
        var subscriptionCalendar = toSubscriptionCalendar(activeAccountCalendar, LocalDate.now());
        var childMoney = toChildMoneyAccount(accountMoney);

        return new ChildMoneyAccount(
                toChildAccountIdentity(childAccountId),
                childIdentity,
                subscriptionCalendar,
                childMoney
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
