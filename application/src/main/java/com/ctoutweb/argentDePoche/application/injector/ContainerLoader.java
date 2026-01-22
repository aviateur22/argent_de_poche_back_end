package com.ctoutweb.argentDePoche.application.injector;

import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
import com.ctoutweb.argentDePoche.application.service.ChildAccountServiceImpl;
import com.ctoutweb.argentDePoche.application.spi.RandomProvider;

import java.lang.reflect.InvocationTargetException;

public class ContainerLoader {
    private final ConstructorInjectorContainer container;

    public ContainerLoader(ConstructorInjectorContainer container) {
        this.container = container;
    }

    public void loadInstance(
            CommandRepository childAccountRepository,
            RandomProvider randomProvider) throws InvocationTargetException, InstantiationException, IllegalAccessException {

        container.register(CommandRepository.class, childAccountRepository);
        container.register(RandomProvider.class, randomProvider);


        ChildAccountServiceImpl ChildAccountService = container.instanciate(ChildAccountServiceImpl.class);
        container.register(ChildAccountServiceImpl.class, ChildAccountService);


    }
}
