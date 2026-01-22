package com.ctoutweb.argenDePoche.infra.adapter.secondaryAdapter;

import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToCoreMapper;
import com.ctoutweb.argenDePoche.infra.util.NumberUtil;
import com.ctoutweb.argentDePoche.application.port.NextChildAccountIdentities;
import com.ctoutweb.argentDePoche.application.port.NextFamilyAccountIdentities;
import com.ctoutweb.argentDePoche.application.spi.NextIdentityProvider;
import org.springframework.stereotype.Component;

@Component
public class NextEntityProviderAdapter implements NextIdentityProvider {
    private final ToCoreMapper toCoreMapper;

    public NextEntityProviderAdapter(ToCoreMapper toCoreMapper) {
        this.toCoreMapper = toCoreMapper;
    }

    @Override
    public NextChildAccountIdentities generateNextChildAccountIdentities() {
        long nextChildId = NumberUtil.generateRandomLongNumberBetweenMinAndMax(1L, 10L);
        long nextAccountId = NumberUtil.generateRandomLongNumberBetweenMinAndMax(1L, 10L);
        return toCoreMapper.toNextChildAccountIdentities(nextAccountId, nextChildId);
    }

    @Override
    public NextFamilyAccountIdentities generateNextFamilyAccountIdentities() {
        long nextFamilyAccountId = NumberUtil.generateRandomLongNumberBetweenMinAndMax(1L, 10L);
        long nextParentId = NumberUtil.generateRandomLongNumberBetweenMinAndMax(1L, 10L);
        long nextFamilyId = NumberUtil.generateRandomLongNumberBetweenMinAndMax(1L, 10L);
        return toCoreMapper.toNextFamilyIdentities(nextFamilyAccountId, nextFamilyId, nextParentId);
    }
}
