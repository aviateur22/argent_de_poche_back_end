package com.ctoutweb.argentDePoche.application.policy;

import com.ctoutweb.argentDePoche.application.exception.FamilyAccountForbiddenException;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccount;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FamilyAccessPolicyTest {

    FamilyAccessPolicy familyAccountPolicy;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        familyAccountPolicy = new FamilyAccessPolicy();
    }

    @Test
    void canAccessFamily_should_return_true() {
        /**
         * Given
         */
        ParentIdentity parent = new ParentIdentity(1L);
        FamilyAccount familyAccount = new FamilyAccount(
                new FamilyAccountIdentity(1L),
                null,
                List.of(
                        parent,
                        new ParentIdentity(2L)
                ),
                List.of()
        );
        /**
         * when
         */
       familyAccountPolicy.checkAccess(
               List.of(parent, new ParentIdentity(2L)
               ), new ParentIdentity(1L)
       );
    }

    @Test
    void canAccessFamily_should_return_false() {
        /**
         * Given
         */
        ParentIdentity parent = new ParentIdentity(1L);
        FamilyAccount familyAccount = new FamilyAccount(
                new FamilyAccountIdentity(1L),
                null,
                List.of(
                        parent,
                        new ParentIdentity(2L)
                ),
                List.of()
        );
        /**
         * when - then
         */
        Exception exception = Assertions.assertThrows(FamilyAccountForbiddenException.class, () -> familyAccountPolicy
                .checkAccess(List.of(
                        parent,
                        new ParentIdentity(2L)), new ParentIdentity(3L)));
        assertEquals("Vous ne pouvez pas accéder à cette famille", exception.getMessage());

    }
}
