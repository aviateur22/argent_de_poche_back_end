package com.ctoutweb.argenDePoche.infra.adapter.primaryAdapter;

import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToCoreMapper;
import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToInfraMapper;
import com.ctoutweb.argentDePoche.application.api.ChildAccountManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ChildAccountManagerAdapter {
    private final ChildAccountManager childAccountManager;
    private final ToCoreMapper toCoreMapper;
    private final ToInfraMapper toInfraMapper;

    public ChildAccountManagerAdapter(ChildAccountManager childAccountManager, ToCoreMapper toCoreMapper, ToInfraMapper toInfraMapper) {
        this.childAccountManager = childAccountManager;
        this.toCoreMapper = toCoreMapper;
        this.toInfraMapper = toInfraMapper;
    }

    /**
     * Creation d'un nouveeau compte d'argent de poche
     *
     * @param parentCreatingChildAccount Lidentifiant du parent faisant la création
     * @param childName Le nom de l'enfant
     * @param savingPath Le path de sauvagarde de l'image
     *
     * @return L'identifiant du compte créé
     */
    public Mono<Long> createChildAccount(long parentCreatingChildAccount, String childName, String savingPath) {
        var parentIdentity = toCoreMapper.toParentIdentity(parentCreatingChildAccount);

        return childAccountManager.createChildMoneyAccount(
            parentIdentity,
            childName,
            savingPath
        ).map(toInfraMapper::toTechnicalId);
    }


}
