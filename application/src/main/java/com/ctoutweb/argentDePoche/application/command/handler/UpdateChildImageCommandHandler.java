package com.ctoutweb.argentDePoche.application.command.handler;

import com.ctoutweb.argentDePoche.application.command.MonoCommandHandler;
import com.ctoutweb.argentDePoche.application.command.dto.command.UpdateChildImageCommand;
import com.ctoutweb.argentDePoche.application.command.helper.LoaderChildAccount;
import com.ctoutweb.argentDePoche.application.configuration.annotation.CoreService;
import com.ctoutweb.argentDePoche.application.configuration.bus.EventBus;
import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.application.policy.ChildAccessPolicy;
import com.ctoutweb.argentDePoche.application.policy.FamilyAccessPolicy;
import reactor.core.publisher.Mono;

/**
 * CommandHandler de mise a jour de l'image du compte de l'enfant
 */
@CoreService
public class UpdateChildImageCommandHandler implements MonoCommandHandler<UpdateChildImageCommand, ChildMoneyAccountIdentity> {
    private final CommandRepository commandRepository;
    private final LoaderChildAccount loaderChildAccount;
    private final FamilyAccessPolicy familyAccessPolicy;
    private final ChildAccessPolicy childAccessPolicy;
    private final EventBus eventBus;

    public UpdateChildImageCommandHandler(
            CommandRepository childAccountRepository,
            LoaderChildAccount loaderChildAccount,
            FamilyAccessPolicy familyAccessPolicy,
            ChildAccessPolicy childAccessPolicy,
            EventBus eventBus) {
        this.commandRepository = childAccountRepository;
        this.loaderChildAccount = loaderChildAccount;
        this.familyAccessPolicy = familyAccessPolicy;
        this.childAccessPolicy = childAccessPolicy;
        this.eventBus = eventBus;
    }

    /**
     * Renvoie l'ancienne image a supprimer
     *
     * @param command Données nécéssaire à l'execution du commandHandler
     *
     * @return L'ancienne image a supprimer
     */
    @Override
    public Mono<ChildMoneyAccountIdentity> handle(UpdateChildImageCommand command) {
//        var parentCreatingChildAccount = command.parentUpdatingChildAccount();
//        var newRandomImageName = command.imageRandomName();
//        var updatedImage = command.newChildImage();
//
//        var family = commandRepository.loadFamilyAccountFromParent(parentCreatingChildAccount)
//                .orElseThrow(() -> new FamilyAccountException("Aucune famille existante"));
//
//        // Vérification authorisation
//        familyAccessPolicy.checkAccess(family.getParentIdentities(), parentCreatingChildAccount);
//
//        // Vérification que le parent peut modifier le compte de l'enfant
//        childAccessPolicy.checkAccess(command.childAccountUpdated(), family.getChildMoneyAccountIds());
//
//        // Chargement de l'argent de poche
//        var childMoneyAccountBeforeUpdate = loaderChildAccount.load(command.childAccountUpdated());
//        var oldImageName = childMoneyAccountBeforeUpdate.getChild().childImage().imageRandomName();
//
//        // Mise a jour de l'aggregat avec les données de l'image
//        ChildMoneyAccount updatedChildAccount = childMoneyAccountBeforeUpdate.updateChildImage(command.imageRandomName());
//        var childAccountUpdatedIdentity = commandRepository.updateChildMoneyAccount(updatedChildAccount);
//
//        // Sauvegarde de l'image
//        eventBus.publish(new SaveImageEvent(newRandomImageName, updatedImage.read()));
//
//        // Suppression de l'image
//        eventBus.publish(new DeleteImageEvent(oldImageName));
//
//        return childAccountUpdatedIdentity;
        return null;
    }
}
