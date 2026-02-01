package com.ctoutweb.argentDePoche.application.command.handler;

import com.ctoutweb.argentDePoche.application.command.MonoCommandHandler;
import com.ctoutweb.argentDePoche.application.command.dto.command.InitializeNextPeriodCommand;
import com.ctoutweb.argentDePoche.application.configuration.annotation.CoreService;
import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
import reactor.core.publisher.Mono;

/**
 * Initialisation d'une nouvelle période pour les comptes d'argent de poche.
 * Tous les comptes d'argent de poche sont concerné par cette initialisation d'une nouvelle période
 * Cette methode est appelée à partir du cron fixé tous les dimanche à minuit
 */
@CoreService
public class InitializeNexPeriodCommandHandler implements MonoCommandHandler<InitializeNextPeriodCommand, Boolean> {
  private final CommandRepository commandRepository;

  public InitializeNexPeriodCommandHandler(CommandRepository commandRepository) {
    this.commandRepository = commandRepository;
  }

  @Override
  public Mono<Boolean> handle(InitializeNextPeriodCommand command) {
    // L'ajout de la nouvelle periode de calendrier ne pourra se faire que
    return commandRepository.initializeNextPeriod();
  }
}
