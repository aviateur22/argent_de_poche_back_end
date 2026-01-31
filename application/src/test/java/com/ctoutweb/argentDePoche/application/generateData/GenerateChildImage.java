package com.ctoutweb.argentDePoche.application.generateData;

import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.childImage.ChildImage;
import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.childImage.ChildImageIdentity;

public class GenerateChildImage {
    private static final ChildImageIdentity CHILD_IMAGE_IDENT = new ChildImageIdentity(1L) {
        @Override
        public String getIdentity() {
            return "1";
        }
    };

    private static final String IMAGE_NAME = "random_name";

    public ChildImage generate() {
        return new ChildImage(IMAGE_NAME);
    }
}
