package com.ctoutweb.argenDePoche.infra.adapter.bus.suscriber;

import com.ctoutweb.argentDePoche.application.configuration.bus.QueryBus;
import com.ctoutweb.argentDePoche.application.query.DisplayChildAccountInfoQueryHandler;
import com.ctoutweb.argentDePoche.application.query.GenerateQrCodeQueryHandler;
import com.ctoutweb.argentDePoche.application.query.LoadChildAccountQueryHandler;
import com.ctoutweb.argentDePoche.application.query.LoadFamilyAccountQueryHandler;
import com.ctoutweb.argentDePoche.application.query.dto.query.DisplayChildAccountInfoQuery;
import com.ctoutweb.argentDePoche.application.query.dto.query.GenerateQrCodeQuery;
import com.ctoutweb.argentDePoche.application.query.dto.query.LoadChildAccountQuery;
import com.ctoutweb.argentDePoche.application.query.dto.query.LoadFamilyAccountQuery;
import org.springframework.stereotype.Component;

@Component
public class QueryBusSuscriber {
    private final QueryBus queryBus;
    private final GenerateQrCodeQueryHandler generateQrCodeQueryHandler;
    private final LoadFamilyAccountQueryHandler loadFamilyAccountQueryHandler;
    private final LoadChildAccountQueryHandler loadChildAccountQueryHandler;
    private final DisplayChildAccountInfoQueryHandler displayChildAccountInfoQueryHandler;

    public QueryBusSuscriber(
            QueryBus queryBus,
            GenerateQrCodeQueryHandler generateQrCodeQueryHandler,
            LoadFamilyAccountQueryHandler loadFamilyAccountQueryHandler,
            LoadChildAccountQueryHandler loadChildAccountQueryHandler,
            DisplayChildAccountInfoQueryHandler displayChildAccountInfoQueryHandler) {
        this.queryBus = queryBus;
      this.generateQrCodeQueryHandler = generateQrCodeQueryHandler;
      this.loadFamilyAccountQueryHandler = loadFamilyAccountQueryHandler;
      this.loadChildAccountQueryHandler = loadChildAccountQueryHandler;
      this.displayChildAccountInfoQueryHandler = displayChildAccountInfoQueryHandler;

      register();
    }

    public void register() {
        queryBus.registerMonoHandler(LoadFamilyAccountQuery.class, loadFamilyAccountQueryHandler);
        queryBus.registerMonoHandler(LoadChildAccountQuery.class, loadChildAccountQueryHandler);
        queryBus.registerMonoHandler(GenerateQrCodeQuery.class, generateQrCodeQueryHandler);
        queryBus.registerMonoHandler(DisplayChildAccountInfoQuery.class, displayChildAccountInfoQueryHandler);
    }
}
