package com.ctoutweb.argentDePoche.application.command.handler;

import com.ctoutweb.argentDePoche.application.command.BaseCommandHandler;
import com.ctoutweb.argentDePoche.application.command.dto.command.StreamChildImageCommand;
import com.ctoutweb.argentDePoche.application.command.helper.LoaderChildAccount;
import com.ctoutweb.argentDePoche.application.configuration.annotation.CoreService;
import com.ctoutweb.argentDePoche.application.policy.ChildAccessPolicy;
import com.ctoutweb.argentDePoche.application.policy.FamilyAccessPolicy;
import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.childImage.ImageExtension;
import reactor.core.publisher.Mono;

@CoreService
public class StreamChildImageCommandHandler extends BaseCommandHandler<StreamChildImageCommand, ImageExtension> {
  private final LoaderChildAccount loaderChildAccount;

  protected StreamChildImageCommandHandler(
          FamilyAccessPolicy familyAccessPolicy,
          ChildAccessPolicy childAccessPolicy,
          CommandRepository commandRepository,
          LoaderChildAccount loaderChildAccount) {
    super(familyAccessPolicy, childAccessPolicy, commandRepository);
    this.loaderChildAccount = loaderChildAccount;
  }

  @Override
  protected Mono<ImageExtension> doHandle(StreamChildImageCommand command) {
    return loaderChildAccount.load(command.childMoneyAccountIdentity())
            .map(childAccount ->
                    childAccount.getChild().childImage().imageExtension()
            );
  }
}
