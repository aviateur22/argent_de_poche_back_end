package com.ctoutweb.argentDePoche.application.query;

import com.ctoutweb.argentDePoche.application.configuration.annotation.CoreService;
import com.ctoutweb.argentDePoche.application.exception.FamilyAccountForbiddenException;
import com.ctoutweb.argentDePoche.application.policy.ChildAccessPolicy;
import com.ctoutweb.argentDePoche.application.policy.FamilyAccessPolicy;
import com.ctoutweb.argentDePoche.application.query.dto.QrCodeDto;
import com.ctoutweb.argentDePoche.application.query.dto.query.GenerateQrCodeQuery;
import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
import com.ctoutweb.argentDePoche.application.spi.QrCodeProvider;
import reactor.core.publisher.Mono;

@CoreService
public class GenerateQrCodeQueryHandler implements MonoQueryHandler<GenerateQrCodeQuery, QrCodeDto> {

  private final QrCodeProvider qrCodeProvider;
  private final ChildAccessPolicy childAccessPolicy;
  private final FamilyAccessPolicy familyAccessPolicy;
  private final CommandRepository commandRepository;

  public GenerateQrCodeQueryHandler(
          QrCodeProvider qrCodeProvider,
          ChildAccessPolicy childAccessPolicy,
          FamilyAccessPolicy familyAccessPolicy, CommandRepository commandRepository) {
    this.qrCodeProvider = qrCodeProvider;
    this.childAccessPolicy = childAccessPolicy;
    this.familyAccessPolicy = familyAccessPolicy;
    this.commandRepository = commandRepository;
  }

  @Override
  public Mono<QrCodeDto> handle(GenerateQrCodeQuery query) {

    var childAccountRequested = query.childMoneyAccountIdentity();
    var parentRequestedChildAccount = query.parentIdentity();

    return commandRepository.loadFamilyAccountFromParent(parentRequestedChildAccount)
        .switchIfEmpty(Mono.error(new FamilyAccountForbiddenException("Aucun compte de famille n'est associé à votre compte")))
        .flatMap(familyAccount -> {
          // Vérification authorisation
          familyAccessPolicy.checkAccess(familyAccount.getParentIdentities(), parentRequestedChildAccount);

          // Vérification que le parent peut accéder au compte de l'enfant
          childAccessPolicy.checkAccess(childAccountRequested, familyAccount.getChildMoneyAccountIds());

          return this.qrCodeProvider.generate(query.url(), query.imageHeightAndWidth())
          .map(QrCodeDto::new);
        });
    }
}
