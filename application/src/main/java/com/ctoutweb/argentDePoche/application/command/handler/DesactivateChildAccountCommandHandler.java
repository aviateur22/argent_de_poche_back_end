package com.ctoutweb.argentDePoche.application.command.handler;

import com.ctoutweb.argentDePoche.application.command.BaseCommandHandler;
import com.ctoutweb.argentDePoche.application.command.dto.command.DesactivateChildAccountCommand;
import com.ctoutweb.argentDePoche.application.configuration.annotation.CoreService;
import com.ctoutweb.argentDePoche.application.policy.ChildAccessPolicy;
import com.ctoutweb.argentDePoche.application.policy.FamilyAccessPolicy;
import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import reactor.core.publisher.Mono;

@CoreService
public class DesactivateChildAccountCommandHandler extends BaseCommandHandler<DesactivateChildAccountCommand, ChildMoneyAccountIdentity> {

  private final CommandRepository commandRepository;

  protected DesactivateChildAccountCommandHandler(
          FamilyAccessPolicy familyAccessPolicy,
          ChildAccessPolicy childAccessPolicy,
          CommandRepository commandRepository) {
    super(familyAccessPolicy, childAccessPolicy, commandRepository);
    this.commandRepository = commandRepository;
  }

  @Override
  protected Mono<ChildMoneyAccountIdentity> doHandle(DesactivateChildAccountCommand command) {
    // chargement des données du compte de l'enfant
    return commandRepository.desactivateAccount(command.childAccountUpdated())
            .map(childMoneyAccountIdentity -> childMoneyAccountIdentity);
  }
}
