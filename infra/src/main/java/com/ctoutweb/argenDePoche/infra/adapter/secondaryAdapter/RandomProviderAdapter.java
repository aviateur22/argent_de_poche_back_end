package com.ctoutweb.argenDePoche.infra.adapter.secondaryAdapter;

import com.ctoutweb.argenDePoche.infra.util.TextUtil;
import com.ctoutweb.argentDePoche.application.spi.RandomProvider;
import org.springframework.stereotype.Component;

@Component
public class RandomProviderAdapter implements RandomProvider {

    @Override
    public String generateUniqueRandomUuid() {
        return TextUtil.getRandomNameUUID();
    }


}
