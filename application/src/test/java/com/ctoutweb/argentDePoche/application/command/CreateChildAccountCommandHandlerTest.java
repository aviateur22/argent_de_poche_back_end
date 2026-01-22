//package com.ctoutweb.argentDePoche.application.command;
//
//import com.ctoutweb.argentDePoche.application.command.dto.command.CreateChildAccountCommand;
//import com.ctoutweb.argentDePoche.application.command.handler.CreateChildAccountCommandHandler;
//import com.ctoutweb.argentDePoche.application.port.NextChildAccountIdentities;
//import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
//import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccount;
//import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
//import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.child.ChildIdentity;
//import com.ctoutweb.argentDePoche.core.domain.exception.FamilyAccountException;
//import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccount;
//import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;
//import com.ctoutweb.argentDePoche.application.policy.FamilyAccessPolicy;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import reactor.core.publisher.Mono;
//import reactor.test.StepVerifier;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//public class CreateChildAccountCommandHandlerTest {
//
//    @Mock
//    CommandRepository commandRepository;
//
//    @Mock
//    FamilyAccessPolicy familyAccessPolicy;
//
//    CreateChildAccountCommandHandler createChildAccountCommandHandler;
//
//    @BeforeEach
//    void init() {
//        MockitoAnnotations.openMocks(this);
//        createChildAccountCommandHandler = new CreateChildAccountCommandHandler(
//                commandRepository,
//                familyAccessPolicy
//        );
//    }
//
//    @Test
//    void should_create_a_new_child_account() {
//        /**
//         * Given
//         */
//        // Mock de la command
//        CreateChildAccountCommand command = mock(CreateChildAccountCommand.class);
//        when(command.parentCreatingChildAccount()).thenReturn(mock(ParentIdentity.class));
//        when(command.childName()).thenReturn("cyril");
//        when(command.defaultImagePath()).thenReturn("path");
//
//        // Mock generate de familyAccount
//        FamilyAccount familyAccount = mock(FamilyAccount.class);
//
//        // Mock la creation d'un compte d'argent de pioche
//        var createdChildAccount = new ChildMoneyAccountIdentity(1L);
//
//        when(commandRepository.loadFamilyAccountFromParent(any(ParentIdentity.class)))
//                .thenReturn(Optional.of(familyAccount));
//
//        // Mock generation identifiant
//        NextChildAccountIdentities generatedIdentities = mock(NextChildAccountIdentities.class);
//        when(generatedIdentities.getNextChildMoneyAccountId()).thenReturn(mock(ChildMoneyAccountIdentity.class));
//        when(generatedIdentities.getNextChildIdentity()).thenReturn(mock(ChildIdentity.class));
//        when(commandRepository.generateNextChildAccountIdentities())
//                .thenReturn((Mono.just(generatedIdentities)));
//
//        // Mock la persisitance du compte enfant qui est créé
//        when(commandRepository.createChildMoneyAccount(any(ChildMoneyAccount.class)))
//                .thenReturn(Mono.just(createdChildAccount));
//
//        /**
//         * When
//         */
//        StepVerifier.create(createChildAccountCommandHandler.handle(command))
//                .expectNext(createdChildAccount)
//                .verifyComplete();
//
//
//        /**
//         * Then
//         */
//
//        verify(commandRepository).loadFamilyAccountFromParent(any(ParentIdentity.class));
//        verify(familyAccessPolicy).checkAccess(anyList(), any(ParentIdentity.class));
//        verify(commandRepository).generateNextChildAccountIdentities();
//        verify(commandRepository).createChildMoneyAccount(any(ChildMoneyAccount.class));
//
//    }
//
//    @Test
//    void createChildAccount_should_throw_when_family_not_found() {
//        /**
//         * Given
//         */
//        // Mock de la command
//        CreateChildAccountCommand command = mock(CreateChildAccountCommand.class);
//        when(command.parentCreatingChildAccount()).thenReturn(mock(ParentIdentity.class));
//        when(command.childName()).thenReturn("cyril");
//        when(command.defaultImagePath()).thenReturn("path");
//
//        // Mock generate de familyAccount
//        FamilyAccount familyAccount = mock(FamilyAccount.class);
//        when(commandRepository.loadFamilyAccountFromParent(any(ParentIdentity.class)))
//                .thenReturn(Mono.empty());
//
//        // Mock generation identifiant
//        NextChildAccountIdentities generatedIdentities = mock(NextChildAccountIdentities.class);
//        when(generatedIdentities.getNextChildMoneyAccountId()).thenReturn(mock(ChildMoneyAccountIdentity.class));
//        when(generatedIdentities.getNextChildIdentity()).thenReturn(mock(ChildIdentity.class));
//        when(commandRepository.generateNextChildAccountIdentities())
//                .thenReturn((Mono.just(generatedIdentities)));
//
//        // Mock la persisitance du compte enfant qui est créé
//        when(commandRepository.createChildMoneyAccount(any(ChildMoneyAccount.class)))
//                .thenReturn(Mono.just(mock(ChildMoneyAccountIdentity.class)));
//
//        /**
//         * When - then
//         */
//        Exception exception = Assertions.assertThrows(FamilyAccountException.class, () -> createChildAccountCommandHandler.handle(command));
//        assertEquals("Aucune famille existante", exception.getMessage());
//    }
//
//    @Test
//    void createChildAccount_should_throw_parent_not_authorized() {
//        /**
//         * Given
//         */
//        // Mock de la command
//        CreateChildAccountCommand command = mock(CreateChildAccountCommand.class);
//        when(command.parentCreatingChildAccount()).thenReturn(mock(ParentIdentity.class));
//        when(command.childName()).thenReturn("cyril");
//        when(command.defaultImagePath()).thenReturn("path");
//
//        // Mock generate de familyAccount
//        FamilyAccount familyAccount = mock(FamilyAccount.class);
//        when(commandRepository.loadFamilyAccountFromParent(any(ParentIdentity.class)))
//                .thenReturn(Optional.of(mock(FamilyAccount.class)));
//
//        // Mock authoriztion modification famille
//      doThrow(new FamilyAccountException(""))
//              .when(familyAccessPolicy)
//              .checkAccess(anyList(), any(ParentIdentity.class));
//
//        // Mock generation identifiant
//        NextChildAccountIdentities generatedIdentities = mock(NextChildAccountIdentities.class);
//        when(generatedIdentities.getNextChildMoneyAccountId()).thenReturn(mock(ChildMoneyAccountIdentity.class));
//        when(generatedIdentities.getNextChildIdentity()).thenReturn(mock(ChildIdentity.class));
//        when(commandRepository.generateNextChildAccountIdentities())
//                .thenReturn((Mono.just(generatedIdentities)));
//
//        // Mock la persisitance du compte enfant qui est créé
//        when(commandRepository.createChildMoneyAccount(any(ChildMoneyAccount.class)))
//                .thenReturn(Mono.just(mock(ChildMoneyAccountIdentity.class)));
//
//        /**
//         * When - then
//         */
//        Exception exception = Assertions.assertThrows(FamilyAccountException.class, () -> createChildAccountCommandHandler.handle(command));
//        verify(familyAccessPolicy).checkAccess(anyList(), any(ParentIdentity.class));
//        verify(commandRepository, never()).generateNextChildAccountIdentities();
//        verify(commandRepository, never()).createChildMoneyAccount(any());
//
//
//    }
//}
