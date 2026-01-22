package com.ctoutweb.argentDePoche.application.generateData;

import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.Parent;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;

public class GenerateParent {

    private static final ParentIdentity PARENT_IDENT = new ParentIdentity(1L) {

        @Override
        public String getIdentity() {
            return "1";
        }
    };


    public Parent generate() {
        return new Parent(
                PARENT_IDENT,
                "celine"
        );
    }
}
