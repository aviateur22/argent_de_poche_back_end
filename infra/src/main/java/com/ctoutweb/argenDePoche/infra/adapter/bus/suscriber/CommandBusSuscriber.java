package com.ctoutweb.argenDePoche.infra.adapter.bus.suscriber;


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
    private final InitializeNexPeriodCommandHandler initializeNexPeriodCommandHandler;
    private final ReinitializeRemainingMoneyCommandHandler reinitializeRemainingMoneyCommandHandler;
    private final StreamChildImageCommandHandler streamChildImageCommandHandler;
    private final UpdateChildNameCommandHandler updateChildNameCommandHandler;
    private final DesactivateChildAccountCommandHandler desactivateChildAccountCommandHandler;

    public CommandBusSuscriber(
            CommandBus commandBus,
            CreateChildAccountCommandHandler createChildAccountCommandHandler,
            AddMoneyMovementCommandHandler addMoneyMovementCommandHandler,
            CreateFamilyAccountCommandHandler createFamilyAccountCommandHandler,
            UpdateChildImageCommandHandler updateChildImageCommandHandler,
            UpdateInitialChildMoneyCommandHandler updateInitialChildMoneyCommandHandler,
            InitializeNexPeriodCommandHandler initializeNexPeriodCommandHandler,
            ReinitializeRemainingMoneyCommandHandler reinitializeRemainingMoneyCommandHandler,
            StreamChildImageCommandHandler streamChildImageCommandHandler,
            UpdateChildNameCommandHandler updateChildNameCommandHandler, DesactivateChildAccountCommandHandler desactivateChildAccountCommandHandler
    ) {
        this.commandBus = commandBus;
        this.createChildAccountCommandHandler = createChildAccountCommandHandler;
        this.addMoneyMovementCommandHandler = addMoneyMovementCommandHandler;
        this.createFamilyAccountCommandHandler = createFamilyAccountCommandHandler;
        this.updateChildImageCommandHandler = updateChildImageCommandHandler;
        this.updateInitialChildMoneyCommandHandler = updateInitialChildMoneyCommandHandler;
        this.initializeNexPeriodCommandHandler = initializeNexPeriodCommandHandler;
        this.reinitializeRemainingMoneyCommandHandler = reinitializeRemainingMoneyCommandHandler;
        this.streamChildImageCommandHandler = streamChildImageCommandHandler;
        this.updateChildNameCommandHandler = updateChildNameCommandHandler;
        this.desactivateChildAccountCommandHandler = desactivateChildAccountCommandHandler;

        // Enregistrement des Handler
      suscribe();
    }

    public void suscribe() {
        commandBus.registerMonoHandler(CreateChildAccountCommand.class, createChildAccountCommandHandler);
        commandBus.registerMonoHandler(AddMoneyMovementCommand.class, addMoneyMovementCommandHandler);
        commandBus.registerMonoHandler(CreateFamilyAccountCommand.class, createFamilyAccountCommandHandler);
        commandBus.registerMonoHandler(UpdateChildImageCommand.class, updateChildImageCommandHandler);
        commandBus.registerMonoHandler(UpdateInitialChildMoneyCommand.class, updateInitialChildMoneyCommandHandler);
        commandBus.registerMonoHandler(InitializeNextPeriodCommand.class, initializeNexPeriodCommandHandler);
        commandBus.registerMonoHandler(ReinitializeRemainingMoneyCommand.class, reinitializeRemainingMoneyCommandHandler);
        commandBus.registerMonoHandler(StreamChildImageCommand.class, streamChildImageCommandHandler);
        commandBus.registerMonoHandler(UpdateChildNameCommand.class, updateChildNameCommandHandler);
        commandBus.registerMonoHandler(DesactivateChildAccountCommand.class, desactivateChildAccountCommandHandler);
    }
}
