package com.ctoutweb.argentDePoche.application.policy;

import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.application.exception.ChildMoneyForbiddenException;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ChildAccessPolicyTest {

    ChildAccessPolicy childAccessPolicy;

    @BeforeEach
    void init() {
        childAccessPolicy = new ChildAccessPolicy();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void canAccessChild_should_return_true() {
        /**
         * Given
         */
        ChildMoneyAccountIdentity firstChildAccount = new ChildMoneyAccountIdentity(1L);
        ChildMoneyAccountIdentity secondChildAccount = new ChildMoneyAccountIdentity(2L);

        var childIds = List.of(firstChildAccount, secondChildAccount);

        /**
         * When
         */
        childAccessPolicy.checkAccess(new ChildMoneyAccountIdentity(1L), childIds);



    }

    @Test
    void canAccessChild_should_return_false() {
        /**
         * Given
         */
        ChildMoneyAccountIdentity firstChildAccount = new ChildMoneyAccountIdentity(1L);
        ChildMoneyAccountIdentity secondChildAccount = new ChildMoneyAccountIdentity(2L);

        var childIds = List.of(firstChildAccount, secondChildAccount);

        /**
         * When Then
         */
        ParentIdentity parentIdentity = new ParentIdentity(1L);
        FamilyAccountIdentity familyAccountId = new FamilyAccountIdentity(1L);
        Exception exception = Assertions.assertThrows(ChildMoneyForbiddenException.class,
                () -> childAccessPolicy.checkAccess(new ChildMoneyAccountIdentity(3L),  childIds));
       assertEquals("Vous ne pouvez pas accéder à ce compte d'argent de poche", exception.getMessage());
    }

}
