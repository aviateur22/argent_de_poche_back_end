package com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate;

import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.family.FamilyIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.family.Family;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;

import java.util.ArrayList;
import java.util.List;

public class FamilyAccount {
    private final FamilyAccountIdentity familyAccountId;
    private final Family family;
    private final List<ParentIdentity> parentIdentities;
    private final List<ChildMoneyAccountIdentity> childMoneyAccountIds;

    public FamilyAccount(
            FamilyAccountIdentity familyAccountId,
            Family family,
            List<ParentIdentity> parentIdentities,
            List<ChildMoneyAccountIdentity> childMoneyAccountIds) {
        this.familyAccountId = familyAccountId;
        this.family = family;
        this.parentIdentities = parentIdentities;
        this.childMoneyAccountIds = childMoneyAccountIds;
    }

    /**
     * Factory permettant de créer une nouvelle famille
     *
     * @param familyAccountIdentiy L'identitifant de la nouvelle famille
     * @param parentCreatingFamily L'identitifant du parent ayanbt créé la famille
     * @param familyIdentity L'identifiant de la nouvelle famille
     * @param familyName Le nom de la famille
     *
     * @return L'aggragt de la nouvelle famille
     */
    public static FamilyAccount create(
            FamilyAccountIdentity familyAccountIdentiy,
            FamilyIdentity familyIdentity,
            ParentIdentity parentCreatingFamily,
            String familyName) {
        final Family createdFamily = Family.create(familyIdentity, familyName);
        final List<ParentIdentity> initialParentIdentities = List.of(parentCreatingFamily);
        final List<ChildMoneyAccountIdentity> initialChildrenMoneyAccountIds = List.of();

        return new FamilyAccount(familyAccountIdentiy, createdFamily, initialParentIdentities, initialChildrenMoneyAccountIds);
    }

    public FamilyAccount addChildAccount(ChildMoneyAccountIdentity createdChildMoneyAccountId) {

        List<ChildMoneyAccountIdentity> updateChildMoneyAccounts =new ArrayList<>(this.childMoneyAccountIds);
        updateChildMoneyAccounts.add(createdChildMoneyAccountId);

        return new FamilyAccount(this.familyAccountId, this.family, this.parentIdentities, updateChildMoneyAccounts);
    }

    public FamilyAccountIdentity getFamilyAccountId() {
        return familyAccountId;
    }

    public Family getFamily() {
        return family;
    }

    public List<ParentIdentity> getParentIdentities() {
        return parentIdentities;
    }

    public List<ChildMoneyAccountIdentity> getChildMoneyAccountIds() {
        return childMoneyAccountIds;
    }
}
