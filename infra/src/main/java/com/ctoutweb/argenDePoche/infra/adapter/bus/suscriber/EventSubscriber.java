package com.ctoutweb.argenDePoche.infra.adapter.bus.suscriber;

import com.ctoutweb.argenDePoche.infra.service.LogService;

import com.ctoutweb.argentDePoche.application.configuration.bus.EventBus;
import com.ctoutweb.argentDePoche.application.configuration.event.LogErrorEvent;
import com.ctoutweb.argentDePoche.application.configuration.event.LogInfoEvent;
import org.springframework.stereotype.Component;

@Component
public class EventSubscriber {
    private final EventBus eventBus;
    private final LogService logService;

    public EventSubscriber(EventBus eventBus, LogService logService) {
        this.eventBus = eventBus;
        this.logService = logService;
        subscribe();
    }

    private void subscribe() {
        eventBus.subscribe(LogErrorEvent.class, event -> logService.errorLog(event.message()));
        eventBus.subscribe(LogInfoEvent.class, event -> logService.infoLog(event.message()));
    }
}
