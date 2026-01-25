package com.ctoutweb.argenDePoche.infra.adapter.primaryAdapter;

import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToCoreMapper;
import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToDtoMapper;
import com.ctoutweb.argenDePoche.infra.adapter.mapper.ToInfraMapper;
import com.ctoutweb.argenDePoche.infra.model.dto.ChildAccountResponseDto;
import com.ctoutweb.argentDePoche.application.api.ChildAccountManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ChildAccountManagerAdapter {
    private final ChildAccountManager childAccountManager;
    private final ToCoreMapper toCoreMapper;
    private final ToInfraMapper toInfraMapper;
    private final ToDtoMapper toDtoMapper;

    public ChildAccountManagerAdapter(ChildAccountManager childAccountManager, ToCoreMapper toCoreMapper, ToInfraMapper toInfraMapper, ToDtoMapper toDtoMapper) {
        this.childAccountManager = childAccountManager;
        this.toCoreMapper = toCoreMapper;
        this.toInfraMapper = toInfraMapper;
      this.toDtoMapper = toDtoMapper;
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

    /**
     * Chargement des données d'un compte d'argent de poche
     *
     * @param parentId L'identifiant du parent faisant la demande
     * @param childAccountId Le compte d'argent de poche demandé
     *
     * @return Les données du compte d'argent de poche
     */
    public Mono<ChildAccountResponseDto> loadChildAccount(long parentId, long childAccountId) {
        var parentIdentity = toCoreMapper.toParentIdentity(parentId);
        var childAccountIdentity = toCoreMapper.toChildAccountIdentity(childAccountId);
        return childAccountManager.loadChildAccount(childAccountIdentity, parentIdentity)
                .map(toDtoMapper::toChildAccountResponseDto);
    }

    /**
     * Initialisation des données pour une nouvelle période
     * Methode croné tous les dimanche minuit
     *
     * @return Boolean
     */
    public Mono<Boolean> initializeNextPeriod() {
        return childAccountManager.initializeNextCalendarPeriod();
    }


}
