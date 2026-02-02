package com.ctoutweb.argentDePoche.application.command.handler;

import com.ctoutweb.argentDePoche.application.command.BaseCommandHandler;
import com.ctoutweb.argentDePoche.application.command.dto.command.ReinitializeRemainingMoneyCommand;
import com.ctoutweb.argentDePoche.application.command.helper.LoaderChildAccount;
import com.ctoutweb.argentDePoche.application.configuration.annotation.CoreService;
import com.ctoutweb.argentDePoche.application.policy.ChildAccessPolicy;
import com.ctoutweb.argentDePoche.application.policy.FamilyAccessPolicy;
import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import reactor.core.publisher.Mono;

@CoreService
public class ReinitializeRemainingMoneyCommandHandler extends BaseCommandHandler<ReinitializeRemainingMoneyCommand, ChildMoneyAccountIdentity> {
  private final CommandRepository commandRepository;
  private final LoaderChildAccount loaderChildAccount;

  protected ReinitializeRemainingMoneyCommandHandler(
          FamilyAccessPolicy familyAccessPolicy,
          ChildAccessPolicy childAccessPolicy,
          CommandRepository commandRepository,
          LoaderChildAccount loaderChildAccount) {
    super(familyAccessPolicy, childAccessPolicy, commandRepository);
    this.commandRepository = commandRepository;
    this.loaderChildAccount = loaderChildAccount;
  }

  @Override
  protected Mono<ChildMoneyAccountIdentity> doHandle(ReinitializeRemainingMoneyCommand command) {
    // chargement des données du compte de l'enfant
      return loaderChildAccount.load(command.childAccountUpdated())
            .flatMap(childAccountMoney -> {
              var childAccountUpdated = childAccountMoney.reinitializeRemainingMoney();
              // Sauvegarde de la mise a jour du compte
              return commandRepository.updateActiveChildMoneyAccount(childAccountUpdated);
            });
  }
}
