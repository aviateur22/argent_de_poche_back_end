package com.ctoutweb.argentDePoche.core.generateData;

import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.child.Child;
import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.child.ChildIdentity;
import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.childImage.ChildImage;


public class GenerateChild {
    private static final ChildIdentity CHILD_IDENT = new ChildIdentity(1L);


    public Child generate() {
        ChildImage childImage = new GenerateChildImage().generate();

        return new Child(CHILD_IDENT, "cyril", childImage);
    }
}
