package com.ctoutweb.argentDePoche.application.query;

import com.ctoutweb.argentDePoche.application.configuration.annotation.CoreService;
import com.ctoutweb.argentDePoche.application.query.dto.ChildAccountDto;
import com.ctoutweb.argentDePoche.application.query.dto.query.DisplayChildAccountInfoQuery;
import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
import com.ctoutweb.argentDePoche.application.repository.QueryRepository;
import reactor.core.publisher.Mono;

@CoreService
public class DisplayChildAccountInfoQueryHandler implements MonoQueryHandler<DisplayChildAccountInfoQuery, ChildAccountDto> {
  private final QueryRepository queryRepository;
  private final CommandRepository commandRepository;

  public DisplayChildAccountInfoQueryHandler(QueryRepository queryRepository, CommandRepository commandRepository) {
    this.queryRepository = queryRepository;
    this.commandRepository = commandRepository;
  }

  @Override
  public Mono<ChildAccountDto> handle(DisplayChildAccountInfoQuery query) {
    var childAccountRequested = query.childAccountRequested();

    return queryRepository.loadActiveChildMoneyAccount(childAccountRequested);

  }
}
