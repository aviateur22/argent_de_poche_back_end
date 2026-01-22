package com.ctoutweb.argenDePoche.infra.adapter.bus.suscribe;


import com.ctoutweb.argentDePoche.application.command.dto.command.*;
import com.ctoutweb.argentDePoche.application.command.handler.*;
import com.ctoutweb.argentDePoche.application.configuration.bus.CommandBus;
import org.springframework.stereotype.Component;

@Component
public class CommandBusSuscriber {
    private final CommandBus commandBus;
    private final CreateChildAccountCommandHandler createChildAccountCommandHandler;
    private final AddMoneyMovementCommandHandler addMoneyMovementCommandHandler;
    private final CreateFamilyAccountCommandHandler createFamilyAccountCommandHandler;
    private final UpdateChildImageCommandHandler updateChildImageCommandHandler;
    private final UpdateInitialChildMoneyCommandHandler updateInitialChildMoneyCommandHandler;

    public CommandBusSuscriber(
            CommandBus commandBus,
            CreateChildAccountCommandHandler createChildAccountCommandHandler,
            AddMoneyMovementCommandHandler addMoneyMovementCommandHandler,
            CreateFamilyAccountCommandHandler createFamilyAccountCommandHandler,
            UpdateChildImageCommandHandler updateChildImageCommandHandler,
            UpdateInitialChildMoneyCommandHandler updateInitialChildMoneyCommandHandler
    ) {
        this.commandBus = commandBus;

        this.createChildAccountCommandHandler = createChildAccountCommandHandler;
        this.addMoneyMovementCommandHandler = addMoneyMovementCommandHandler;
        this.createFamilyAccountCommandHandler = createFamilyAccountCommandHandler;
        this.updateChildImageCommandHandler = updateChildImageCommandHandler;
        this.updateInitialChildMoneyCommandHandler = updateInitialChildMoneyCommandHandler;

        suscribe();
    }

    public void suscribe() {
        commandBus.registerMonoHandler(CreateChildAccountCommand.class, createChildAccountCommandHandler);
        commandBus.registerMonoHandler(AddMoneyMovementCommand.class, addMoneyMovementCommandHandler);
        commandBus.registerMonoHandler(CreateFamilyAccountCommand.class, createFamilyAccountCommandHandler);
        commandBus.registerMonoHandler(UpdateChildImageCommand.class, updateChildImageCommandHandler);
        commandBus.registerMonoHandler(UpdateInitialChildMoneyCommand.class, updateInitialChildMoneyCommandHandler);
    }
}
