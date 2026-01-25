package com.ctoutweb.argenDePoche.infra.adapter.mapper;

import com.ctoutweb.argenDePoche.infra.model.childAccount.calendar.PeriodSubscription;
import com.ctoutweb.argenDePoche.infra.repository.entity.*;
import com.ctoutweb.argentDePoche.core.domain.base.identity.Ident;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccount;
import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.child.Child;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccount;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;
import org.springframework.stereotype.Component;

@Component
public class ToInfraMapper {

    /**
     * Renvoie un identifiant du domaine vers l'identifiant technique
     *
     * @param domainIdentity Lidentifiant a mapper vers l'identifiant technique
     *
     * @return L'identifiant technique
     */
    public long toTechnicalId(Ident<Long> domainIdentity) {
        return Long.parseLong(domainIdentity.getIdentity());
    }

    /**
     * Renvoie un entité de type Child entity
     *
     * @param childMoneyAccount Les données permettants de construire l'entité ChildEntity
     */
    public ChildEntity toChildEntityToUpdate(ChildMoneyAccount childMoneyAccount) {
        ChildEntity childEntity = new ChildEntity();

        childEntity.setId(toTechnicalId(childMoneyAccount.getChild().childIdentity()));
        childEntity.setChildAccountId(toTechnicalId(childMoneyAccount.getChildMoneyAccountId()));
        childEntity.setNickname(childMoneyAccount.getChild().firstName());
        return childEntity;
    }

    /**
     * Renvoie un entité de type Child entity qui est a persister en base
     *
     * @param childtoCreate Les données de l'enfant
     * @param childAccountId L'identifiant technique du compte d'argent de poche
     *
     * @return ChildEntity
     */
    public ChildEntity toChildEntityToCreate(Child childtoCreate, long childAccountId, long childImageId) {
        ChildEntity childEntity = new ChildEntity();
        childEntity.setChildAccountId(childAccountId);
        childEntity.setChildImageId(childImageId);
        childEntity.setNickname(childtoCreate.firstName());
        return childEntity;
    }

    /**
     * Renvoie une entité de type ChildAccountCalendarEntity
     *
     * @param childMoneyAccount Les données permettants de construire l'entité ChildAccountCalendarEntity
     *
     * @return Une instance ChildAccountCalendarEntity
     */
    public ChildAccountCalendarEntity toCalendarEntity(ChildMoneyAccount childMoneyAccount) {
        ChildAccountCalendarEntity calendar = new ChildAccountCalendarEntity();
        PeriodSubscription periodSubscription = toPeriodSubscription(
                childMoneyAccount.getCalendarSubscription()
                        .periodSubscription()
                        .getPeriodSubscriptionText());

        calendar.setChildAccountId(toTechnicalId(childMoneyAccount.getChildMoneyAccountId()));
        calendar.setCalendarPeriod(periodSubscription.name());
        calendar.setPeriodStartDay(childMoneyAccount.getCalendarSubscription().startDay());
        calendar.setPeriodEndDay(childMoneyAccount.getCalendarSubscription().endDay());
        return calendar;

    }

    /**
     * Renvoie une entité de type ChildAccountCalendarEntity a perister en base
     *
     * @param childMoneyAccount Les données du compte a créer
     *
     * @return ChildAccountCalendarEntity
     */
    public ChildAccountCalendarEntity toCalendarEntityToCreate(ChildMoneyAccount childMoneyAccount, long childAccountId) {
        ChildAccountCalendarEntity calendar = new ChildAccountCalendarEntity();
        PeriodSubscription periodSubscription = toPeriodSubscription(
                childMoneyAccount.getCalendarSubscription()
                        .periodSubscription()
                        .getPeriodSubscriptionText());

        calendar.setChildAccountId(childAccountId);
        calendar.setCalendarPeriod(periodSubscription.name());
        calendar.setPeriodStartDay(childMoneyAccount.getCalendarSubscription().startDay());
        calendar.setPeriodEndDay(childMoneyAccount.getCalendarSubscription().endDay());
        return calendar;

    }

    /**
     * Renvoie une entité ChildMoneyEntity
     *
     * @param childMoneyAccount L'aggregat du compte d'argent de poche
     * @param calendarId L'identifiant du calendrier
     *
     * @return ChildMoneyEntity
     */
    public ChildMoneyEntity toChildMoneyEntity(ChildMoneyAccount childMoneyAccount, long calendarId) {
        ChildMoneyEntity childAccountMoneyEntity = new ChildMoneyEntity();
        childAccountMoneyEntity.setAccountCalendarId(calendarId);
        childAccountMoneyEntity.setChildAccountId(toTechnicalId(childMoneyAccount.getChildMoneyAccountId()));
        childAccountMoneyEntity.setRemainingMoney(childMoneyAccount.getChildMoney().remainingMoney().remainingMoney());
        childAccountMoneyEntity.setMoneyAtPeriodStart(childMoneyAccount.getChildMoney().childMoneyAtPeriodStart());
        return childAccountMoneyEntity;
    }

    /**
     * Renvoie un entity ChildMoneyEntity qui est a créer
     *
     * @param childMoneyAccount Les données sur la création
     * @param childAccountId L'identifiant technique du cmpte d'argent de poche
     *
     * @return ChildMoneyEntity
     */
    public ChildMoneyEntity toChildMoneyEntityToCreate(ChildMoneyAccount childMoneyAccount, long childAccountId) {
        ChildMoneyEntity childAccountMoneyEntity = new ChildMoneyEntity();
        childAccountMoneyEntity.setChildAccountId(childAccountId);
        childAccountMoneyEntity.setRemainingMoney(childMoneyAccount.getChildMoney().remainingMoney().remainingMoney());
        childAccountMoneyEntity.setMoneyAtPeriodStart(childMoneyAccount.getChildMoney().childMoneyAtPeriodStart());
        return childAccountMoneyEntity;
    }

    /**
     * Renvoie une entity ChildAccountEntity
     *
     * @param includeChildAccountId Fault-il inclure identifiant dans l'entité renvoyé(Si oui la requete gé,éré par ORM sera un UPDATE)
     * @param childMoneyAccount Le compte d'argent de poche permettant de mapper vers ChildAccountEntity
     * @param familyAccount Le compte de la famille
     *
     * @return ChildAccountEntity
     */
    public ChildAccountEntity toChildAccountEntity(ChildMoneyAccount childMoneyAccount, FamilyAccount familyAccount, boolean includeChildAccountId) {
        ChildAccountEntity childAccountEntity = new ChildAccountEntity();

        if(includeChildAccountId)
            childAccountEntity.setId(toTechnicalId(childMoneyAccount.getChildMoneyAccountId()));

        childAccountEntity.setFamilyAccountId(toTechnicalId(familyAccount.getFamilyAccountId()));
        return childAccountEntity;
    }

    /**
     * Renvoie une entité
     *
     * @param childMoneyAccount Données du compte contant l'image
     *
     * @return ChildImageEntity
     */
    public ChildImageEntity toChildImageEntity(ChildMoneyAccount childMoneyAccount) {
        var childImage = childMoneyAccount.getChild().childImage();
       ChildImageEntity childImageEntity = new ChildImageEntity();
       childImageEntity.setImageName(childImage.imageRandomName());
       return childImageEntity;
    }

    /**
     * Renvoie une entité FamilyAccountEntity pour la creation d'un nouveau compte de famille
     *
     * @return FamilyAccountEntity
     */
    public FamilyAccountEntity toFamilyAccountToCreateEntity() {
        return new FamilyAccountEntity();
    }

    /**
     * Renvoie une entité FamilyEntity pour la creation d'une nouvelle famille
     *
     * @param familyAccountId L'identitifant technique du compte de famille qui a été créé
     * @param familyName Le nom de la famille
     *
     * @return FamilyEntity
     */
    public FamilyEntity toFamilyToCreateEntity(Long familyAccountId, String familyName) {
        FamilyEntity familyToCreate = new FamilyEntity();
        familyToCreate.setFamilyAccountId(familyAccountId);
        familyToCreate.setName(familyName);
        return familyToCreate;
    }

    /**
     * Renvoie une entité ParentFamilyAccountEntity
     *
     * @param parentId L'identifiant technique du parent qui doit ^etre associé au compte de famille
     * @param familyAccountId L'identifiant technique du compte de famille recvnt le parent
     *
     * @return ParentFamilyAccountEntity
     */
    public ParentFamilyAccountEntity toParentFamilyAccountEntity(ParentIdentity parentId, long familyAccountId) {
       ParentFamilyAccountEntity parentFamilyAccount = new ParentFamilyAccountEntity();
       parentFamilyAccount.setParentId(toTechnicalId(parentId));
       parentFamilyAccount.setFamilyAccountId(familyAccountId);
       return parentFamilyAccount;
    }

    /**
     * Renvoie la PeriodSubscription
     *
     * @param periodSubscription La periode de souscription d'argent
     *
     * @return PeriodSubscription
     */
    private PeriodSubscription toPeriodSubscription(String periodSubscription) {
        return PeriodSubscription.findPeriodSubscription(periodSubscription);
    }
}
