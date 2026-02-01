package com.ctoutweb.argenDePoche.infra.service.impl;

import com.ctoutweb.argenDePoche.infra.adapter.primaryAdapter.ChildAccountUseCaseAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Service
public class AdminServiceImpl implements com.ctoutweb.argenDePoche.infra.service.AdminService {
  private static final Logger LOGGER = LogManager.getLogger();

  private final TransactionalOperator txOperator;
  private final ChildAccountUseCaseAdapter childAccountUseCaseAdapter;
  public AdminServiceImpl(TransactionalOperator txOperator, ChildAccountUseCaseAdapter childAccountManager) {
    this.txOperator = txOperator;
    this.childAccountUseCaseAdapter = childAccountManager;
  }

  //second minute heure day-of-month month day-of-week
  @Scheduled(cron = "0 0 0 ? * SUN")
  @Override
  public Mono<Boolean> initializeNextPeriod() {
    LOGGER.info("Initialisation de la periode suivante");
    return txOperator.transactional(childAccountUseCaseAdapter.initializeNextPeriod())
            .doOnSuccess(success -> LOGGER.info("Initialisation de la période suivante s'est bien passé"));
  }
}
