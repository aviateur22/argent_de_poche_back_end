package com.ctoutweb.argentDePoche.core.domain.familyAccount;

import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccount;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.family.FamilyIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.family.Family;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class FamilyAccountTest {
    FamilyAccountIdentity familyAccountId = new FamilyAccountIdentity(1L);
    FamilyIdentity familyIdentity = new FamilyIdentity(1L);
    Family familyName = new Family(familyIdentity, "familyName");
    List<ParentIdentity> parentIdentities = List.of(new ParentIdentity(1L));
    List<ChildMoneyAccountIdentity> childMoneyAccountIds = List.of(new ChildMoneyAccountIdentity(1L));

    FamilyAccount familyAccount = new FamilyAccount(
            familyAccountId,
            familyName,
            parentIdentities,
            childMoneyAccountIds);
    @Test
    void add_new_child_account_to_family_account() {
        /**
         * Given
         */
        ChildMoneyAccountIdentity accountIdentityToAdd = new ChildMoneyAccountIdentity(1L);

        /**
         * when
         */
        var updateFamilyAccount = familyAccount.addChildAccount(accountIdentityToAdd);

        /**
         * then
         */
        Assertions.assertEquals(2, updateFamilyAccount.getChildMoneyAccountIds().size());


    }
}
