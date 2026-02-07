package com.ctoutweb.argentDePoche.application.command.handler;

import com.ctoutweb.argentDePoche.application.command.BaseCommandHandler;
import com.ctoutweb.argentDePoche.application.command.dto.UpdatedChildImage;
import com.ctoutweb.argentDePoche.application.command.dto.command.UpdateChildImageCommand;
import com.ctoutweb.argentDePoche.application.command.helper.LoaderChildAccount;
import com.ctoutweb.argentDePoche.application.configuration.annotation.CoreService;
import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccount;
import com.ctoutweb.argentDePoche.application.policy.ChildAccessPolicy;
import com.ctoutweb.argentDePoche.application.policy.FamilyAccessPolicy;
import reactor.core.publisher.Mono;

/**
 * CommandHandler de mise a jour de l'image du compte de l'enfant
 */
@CoreService
public class UpdateChildImageCommandHandler extends BaseCommandHandler<UpdateChildImageCommand, UpdatedChildImage> {
    private final CommandRepository commandRepository;
    private final LoaderChildAccount loaderChildAccount;

    public UpdateChildImageCommandHandler(
            CommandRepository commandRepository,
            LoaderChildAccount loaderChildAccount,
            FamilyAccessPolicy familyAccessPolicy,
            ChildAccessPolicy childAccessPolicy) {
        super(familyAccessPolicy, childAccessPolicy, commandRepository);
        this.commandRepository = commandRepository;
        this.loaderChildAccount = loaderChildAccount;
    }

    @Override
    protected Mono<UpdatedChildImage> doHandle(UpdateChildImageCommand command) {
        return loaderChildAccount.load(command.childAccountUpdated())
                .flatMap(childAccount -> {
                    var initialImageName = childAccount.getChild().childImage().imageRandomName();
                    ChildMoneyAccount updatedChildAccount = childAccount.updateChildImage(
                            command.imageRandomName(),
                            command.imageExtension()
                    );
                    return commandRepository.updateActiveChildMoneyAccount(updatedChildAccount)
                            .thenReturn(new UpdatedChildImage(
                                    childAccount.getChildMoneyAccountId(),
                                    command.imageRandomName(),
                                    initialImageName
                            ));
                });
    }
}
