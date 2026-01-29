package com.ctoutweb.argentDePoche.application.command;

import com.ctoutweb.argentDePoche.application.command.dto.command.CreateChildAccountCommand;
import com.ctoutweb.argentDePoche.application.command.handler.CreateChildAccountCommandHandler;
import com.ctoutweb.argentDePoche.application.port.NextChildAccountIdentities;
import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
import com.ctoutweb.argentDePoche.application.spi.NextIdentityProvider;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccount;
import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.child.ChildIdentity;
import com.ctoutweb.argentDePoche.core.domain.exception.FamilyAccountException;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccount;
import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;
import com.ctoutweb.argentDePoche.application.policy.FamilyAccessPolicy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CreateChildAccountCommandHandlerTest {

    @Mock
    CommandRepository commandRepository;

    @Mock
    FamilyAccessPolicy familyAccessPolicy;

    @Mock
    NextIdentityProvider nextIdentityProvider;


    CreateChildAccountCommandHandler createChildAccountCommandHandler;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        createChildAccountCommandHandler = new CreateChildAccountCommandHandler(
                commandRepository,
                familyAccessPolicy,
                nextIdentityProvider
        );
    }

    @Test
    void should_create_a_new_child_account() {
        /**
         * Given
         */
        // Mock de la command
        CreateChildAccountCommand command = mock(CreateChildAccountCommand.class);
        when(command.parentCreatingChildAccount()).thenReturn(mock(ParentIdentity.class));
        when(command.childName()).thenReturn("cyril");

        // Mock generate de familyAccount
        FamilyAccount familyAccount = mock(FamilyAccount.class);

        // Mock la creation d'un compte d'argent de pioche
        var createdChildAccount = new ChildMoneyAccountIdentity(1L);

        when(commandRepository.loadFamilyAccountFromParent(any(ParentIdentity.class)))
                .thenReturn(Mono.just(familyAccount));

        // Mock generation identifiant
        NextChildAccountIdentities generatedIdentities = mock(NextChildAccountIdentities.class);
        when(generatedIdentities.getNextChildMoneyAccountId()).thenReturn(mock(ChildMoneyAccountIdentity.class));
        when(generatedIdentities.getNextChildIdentity()).thenReturn(mock(ChildIdentity.class));
        when(nextIdentityProvider.generateNextChildAccountIdentities())
                .thenReturn(generatedIdentities);

        // Mock la persisitance du compte enfant qui est créé
        when(commandRepository.createChildMoneyAccount(any(ChildMoneyAccount.class), any(FamilyAccount.class)))
                .thenReturn(Mono.just(createdChildAccount));

        /**
         * When
         */
        StepVerifier.create(createChildAccountCommandHandler.handle(command))
                .expectNext(createdChildAccount)
                .verifyComplete();


        /**
         * Then
         */

        verify(commandRepository).loadFamilyAccountFromParent(any(ParentIdentity.class));
        verify(familyAccessPolicy).checkAccess(anyList(), any(ParentIdentity.class));
        verify(nextIdentityProvider).generateNextChildAccountIdentities();
        verify(commandRepository).createChildMoneyAccount(any(ChildMoneyAccount.class), any(FamilyAccount.class));

    }

    @Test
    void createChildAccount_should_throw_when_family_not_found() {
        /**
         * Given
         */
        // Mock de la command
        CreateChildAccountCommand command = mock(CreateChildAccountCommand.class);
        when(command.parentCreatingChildAccount()).thenReturn(mock(ParentIdentity.class));
        when(command.childName()).thenReturn("cyril");

        // Mock generate de familyAccount
        FamilyAccount familyAccount = mock(FamilyAccount.class);
        when(commandRepository.loadFamilyAccountFromParent(any(ParentIdentity.class)))
                .thenReturn(Mono.empty());

        // Mock generation identifiant
        NextChildAccountIdentities generatedIdentities = mock(NextChildAccountIdentities.class);
        when(generatedIdentities.getNextChildMoneyAccountId()).thenReturn(mock(ChildMoneyAccountIdentity.class));
        when(generatedIdentities.getNextChildIdentity()).thenReturn(mock(ChildIdentity.class));
        when(nextIdentityProvider.generateNextChildAccountIdentities())
                .thenReturn(generatedIdentities);

        // Mock la persisitance du compte enfant qui est créé
        when(commandRepository.createChildMoneyAccount(any(ChildMoneyAccount.class), any(FamilyAccount.class)))
                .thenReturn(Mono.just(mock(ChildMoneyAccountIdentity.class)));

        /**
         * When - then
         */
        var errorMessage = "Aucune famille existante";
        StepVerifier.create(createChildAccountCommandHandler.handle(command))
            .expectErrorMatches(ex -> ex instanceof FamilyAccountException && ex.getMessage().equals(errorMessage))
            .verify();

    }

    @Test
    void createChildAccount_should_throw_parent_not_authorized() {
        /**
         * Given
         */
        // Mock de la command
        CreateChildAccountCommand command = mock(CreateChildAccountCommand.class);
        when(command.parentCreatingChildAccount()).thenReturn(mock(ParentIdentity.class));
        when(command.childName()).thenReturn("cyril");

        // Mock generate de familyAccount
        FamilyAccount familyAccount = mock(FamilyAccount.class);
        when(commandRepository.loadFamilyAccountFromParent(any(ParentIdentity.class)))
                .thenReturn(Mono.just(mock(FamilyAccount.class)));

        // Mock authoriztion modification famille
        var errorMessage = "Vous ne pouvez pas accéder à cette famille";
        doThrow(new FamilyAccountException(errorMessage))
              .when(familyAccessPolicy)
              .checkAccess(anyList(), any(ParentIdentity.class));

        // Mock generation identifiant
        NextChildAccountIdentities generatedIdentities = mock(NextChildAccountIdentities.class);
        when(generatedIdentities.getNextChildMoneyAccountId()).thenReturn(mock(ChildMoneyAccountIdentity.class));
        when(generatedIdentities.getNextChildIdentity()).thenReturn(mock(ChildIdentity.class));
        when(nextIdentityProvider.generateNextChildAccountIdentities())
                .thenReturn(generatedIdentities);

        // Mock la persisitance du compte enfant qui est créé
        when(commandRepository.createChildMoneyAccount(any(ChildMoneyAccount.class), any(FamilyAccount.class)))
                .thenReturn(Mono.just(mock(ChildMoneyAccountIdentity.class)));

        /**
         * When - then
         */
        StepVerifier.create(createChildAccountCommandHandler.handle(command))
            .expectErrorMatches(ex -> ex instanceof FamilyAccountException &&  ex.getMessage().equals(errorMessage))
            .verify();

        verify(familyAccessPolicy).checkAccess(anyList(), any(ParentIdentity.class));
        verify(nextIdentityProvider, never()).generateNextChildAccountIdentities();
        verify(commandRepository, never()).createChildMoneyAccount(any(), any());


    }
}
