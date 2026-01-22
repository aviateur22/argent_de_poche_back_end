package com.ctoutweb.argenDePoche.infra.adapter.bus.suscribe;

import com.ctoutweb.argenDePoche.infra.service.ImageService;
import com.ctoutweb.argenDePoche.infra.service.LogService;
import com.ctoutweb.argenDePoche.infra.service.impl.ImageServiceImpl;

import com.ctoutweb.argentDePoche.application.configuration.bus.EventBus;
import com.ctoutweb.argentDePoche.application.configuration.event.DeleteImageEvent;
import com.ctoutweb.argentDePoche.application.configuration.event.LogErrorEvent;
import com.ctoutweb.argentDePoche.application.configuration.event.LogInfoEvent;
import com.ctoutweb.argentDePoche.application.configuration.event.SaveImageEvent;
import org.springframework.stereotype.Component;

@Component
public class EventSubscriber {
    private final EventBus eventBus;
    private final ImageService imageService;
    private final LogService logService;

    public EventSubscriber(EventBus eventBus, ImageServiceImpl imageService, LogService logService) {
        this.eventBus = eventBus;
        this.imageService = imageService;
        this.logService = logService;
        subscribe();
    }

    private void subscribe() {
        eventBus.subscribe(LogErrorEvent.class, event -> logService.errorLog(event.message()));
        eventBus.subscribe(LogInfoEvent.class, event -> logService.infoLog(event.message()));
        eventBus.subscribe(SaveImageEvent.class, event -> imageService.saveImage(event.imageName(), event.updatedImageBytes()));
        eventBus.subscribe(DeleteImageEvent.class, event -> imageService.deleteImage(event.imageName()));
    }
}
