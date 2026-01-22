//package com.ctoutweb.argentDePoche.application.command;
//
//import com.ctoutweb.argentDePoche.application.command.dto.command.AddMoneyMovementCommand;
//import com.ctoutweb.argentDePoche.application.command.handler.AddMoneyMovementCommandHandler;
//import com.ctoutweb.argentDePoche.application.command.helper.LoaderChildAccount;
//import com.ctoutweb.argentDePoche.application.port.AddMoneyMovement;
//import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
//import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccount;
//import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
//import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account.MoneyMovement;
//import com.ctoutweb.argentDePoche.core.domain.exception.FamilyAccountException;
//import com.ctoutweb.argentDePoche.core.domain.exception.MoneyMovementException;
//import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccount;
//import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
//import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;
//import com.ctoutweb.argentDePoche.application.policy.ChildAccessPolicy;
//import com.ctoutweb.argentDePoche.application.policy.FamilyAccessPolicy;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import reactor.core.publisher.Mono;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//public class AddMoneyMovementCommandHandlerTest {
//    @Mock
//    CommandRepository commandRepository;
//
//    @Mock
//    FamilyAccessPolicy familyAccessPolicy;
//
//    @Mock
//    ChildAccessPolicy childAccessPolicy;
//
//    @Mock
//    LoaderChildAccount loaderChildAccount;
//
//
//    AddMoneyMovementCommandHandler addMoneyMovementCommandHandler;
//
//    @BeforeEach
//    void init() {
//        MockitoAnnotations.openMocks(this);
//        addMoneyMovementCommandHandler = new AddMoneyMovementCommandHandler(
//               loaderChildAccount,
//               commandRepository,
//               familyAccessPolicy,
//               childAccessPolicy
//        );
//    }
//
//    @Test
//    void handle_should_throw_when_family_not_avail() {
//
//        /**
//         * Given
//         */
//        ChildMoneyAccount childAccountResult = mock(ChildMoneyAccount.class);
//
//        // Mock de la commande
//        FamilyAccountIdentity familyAccountIdentiy = new FamilyAccountIdentity(1L);
//        ParentIdentity parentIdentity = new ParentIdentity(1L);
//        AddMoneyMovementCommand command = mock(AddMoneyMovementCommand.class);
//        when(command.parentAddingMoneyMovement()).thenReturn(parentIdentity);
//
//        // Mock chathement famille en echec
//        when(commandRepository.loadFamilyAccountFromParent(parentIdentity))
//                .thenReturn(Mono.empty());
//
//        // Mock chargement aggregat compte enfant
//        when(loaderChildAccount.load(any(ChildMoneyAccountIdentity.class)))
//                .thenReturn(childAccountResult);
//
//        /**
//         * When - then
//         */
//        Exception exception = Assertions.assertThrows(FamilyAccountException.class, () -> addMoneyMovementCommandHandler.handle(command));
//        assertEquals("Aucune famille existante", exception.getMessage());
//    }
//
//    @Test
//    void handle_should_throw_when_money_movement_is_negative() {
//        /**
//         * Given
//         */
//        FamilyAccountIdentity familyAccountIdentiy = new FamilyAccountIdentity(1L);
//        ParentIdentity parentUpdatedChildAccount = new ParentIdentity(1L);
//        ChildMoneyAccountIdentity childAccountIdentityToBeUpdated = new ChildMoneyAccountIdentity(1L);
//
//        // Donnée recu sur le mouvement d'argent devant créer une exception
//        AddMoneyMovement addMoneyMovement = new AddMoneyMovement() {
//            @Override
//            public BigDecimal getFluctuationPrice() {
//                return BigDecimal.valueOf(-2.5);
//            }
//
//            @Override
//            public String getMovementActionCode() {
//                return "AM";
//            }
//
//            @Override
//            public String getMovementReasonCode() {
//                return "RS";
//            }
//
//            @Override
//            public ParentIdentity getInitiatedByParent() {
//                return null;
//            }
//        };
//
//        ChildMoneyAccount childMoneyAccountBeforeUpdate = mock(ChildMoneyAccount.class);
//        ChildMoneyAccount childMoneyAccountUpdated = mock(ChildMoneyAccount.class);
//
//        // Mock de la commande
//        AddMoneyMovementCommand command = mock(AddMoneyMovementCommand.class);
//        when(command.parentAddingMoneyMovement()).thenReturn(parentUpdatedChildAccount);
//        when(command.childAccountUpdated()).thenReturn(childAccountIdentityToBeUpdated);
//        when(command.moneyMovementToAdd()).thenReturn(addMoneyMovement);
//        when(command.getParentUpdatedChildAccount()).thenReturn(parentUpdatedChildAccount);
//        when(command.getChildAccountUpdated()).thenReturn(childAccountIdentityToBeUpdated);
//
//        // Mock aggregat Famille
//        var parents = List.of(new ParentIdentity(1L));
//        var children  = List.of(new ChildMoneyAccountIdentity(1L));
//        var familyAggregate = mock(FamilyAccount.class);
//        when(familyAggregate.getParentIdentities()).thenReturn(parents);
//        when(familyAggregate.getChildMoneyAccountIds()).thenReturn(children);
//
//        //Mock pour commandRepository.loadFamilyAccountAggregate
//        when(commandRepository.loadFamilyAccountFromParent(parentUpdatedChildAccount))
//                .thenReturn(Optional.of(familyAggregate));
//
//        //Mock pour commandRepository.loadFamilyAccountAggregate
//        when(loaderChildAccount.load(any(ChildMoneyAccountIdentity.class)))
//                .thenReturn(childMoneyAccountBeforeUpdate);
//
//        // Mock mise a jour du compte de l'enfant
//        when(childMoneyAccountBeforeUpdate.addMoneyMovement(any(MoneyMovement.class)))
//                .thenReturn(childMoneyAccountUpdated);
//
//        // Mock de la persistence de la mise a jour
//        when(commandRepository.updateChildMoneyAccount(childMoneyAccountUpdated))
//                .thenReturn(childAccountIdentityToBeUpdated);
//
//
//        /**
//         * When - then
//         */
//        AddMoneyMovementCommandHandler handler = new AddMoneyMovementCommandHandler(
//                loaderChildAccount,
//                commandRepository,
//                familyAccessPolicy,
//                childAccessPolicy
//        );
//        Exception exception = Assertions.assertThrows(MoneyMovementException.class, () -> handler.handle(command));
//        assertEquals("Le mouvement d'argent ne peut pas être négatif", exception.getMessage());
//    }
//
//    @Test
//    void handle_should_add_the_new_money_movement() {
//        /**
//         * Given
//         */
//        FamilyAccountIdentity familyAccountIdentiy = new FamilyAccountIdentity(1L);
//        ParentIdentity parentUpdatedChildAccount = new ParentIdentity(1L);
//        ChildMoneyAccountIdentity childAccountIdentityToBeUpdated = new ChildMoneyAccountIdentity(1L);
//        // Information recu sur le mouvement d'argent a ajouter
//        AddMoneyMovement addMoneyMovement = new AddMoneyMovement() {
//            @Override
//            public BigDecimal getFluctuationPrice() {
//                return BigDecimal.valueOf(2.5);
//            }
//
//            @Override
//            public String getMovementActionCode() {
//                return "AM";
//            }
//
//            @Override
//            public String getMovementReasonCode() {
//                return "RS";
//            }
//
//            @Override
//            public ParentIdentity getInitiatedByParent() {
//                return null;
//            }
//        };
//        ChildMoneyAccount childMoneyAccountBeforeUpdate = mock(ChildMoneyAccount.class);
//        ChildMoneyAccount childMoneyAccountUpdated = mock(ChildMoneyAccount.class);
//
//        // Mock de la commande
//        AddMoneyMovementCommand command = mock(AddMoneyMovementCommand.class);
//        when(command.parentAddingMoneyMovement()).thenReturn(parentUpdatedChildAccount);
//        when(command.childAccountUpdated()).thenReturn(childAccountIdentityToBeUpdated);
//        when(command.moneyMovementToAdd()).thenReturn(addMoneyMovement);
//        when(command.getParentUpdatedChildAccount()).thenReturn(parentUpdatedChildAccount);
//        when(command.getChildAccountUpdated()).thenReturn(childAccountIdentityToBeUpdated);
//
//        // Mock aggregat Famille
//        var parents = List.of(new ParentIdentity(1L));
//        var children  = List.of(new ChildMoneyAccountIdentity(1L));
//        var familyAggregate = mock(FamilyAccount.class);
//        when(familyAggregate.getParentIdentities()).thenReturn(parents);
//        when(familyAggregate.getChildMoneyAccountIds()).thenReturn(children);
//
//        //Mock pour commandRepository.loadFamilyAccountAggregate
//        when(commandRepository.loadFamilyAccountFromParent(parentUpdatedChildAccount))
//                .thenReturn(Optional.of(familyAggregate));
//
//        //Mock pour commandRepository.loadFamilyAccountAggregate
//        when(loaderChildAccount.load(any(ChildMoneyAccountIdentity.class)))
//                .thenReturn(childMoneyAccountBeforeUpdate);
//
//        // Mock mise a jour du compte de l'enfant
//        when(childMoneyAccountBeforeUpdate.addMoneyMovement(any(MoneyMovement.class)))
//                .thenReturn(childMoneyAccountUpdated);
//
//        // Mock de la persistence de la mise a jour
//        when(commandRepository.updateChildMoneyAccount(childMoneyAccountUpdated))
//                .thenReturn(childAccountIdentityToBeUpdated);
//
//
//        /**
//         * When
//         */
//        AddMoneyMovementCommandHandler handler = new AddMoneyMovementCommandHandler(
//                loaderChildAccount,
//                commandRepository,
//                familyAccessPolicy,
//                childAccessPolicy
//        );
//        handler.handle(command);
//
//        /**
//         * Then
//         */
//        verify(familyAccessPolicy).checkAccess(parents, parentUpdatedChildAccount);
//        verify(childAccessPolicy).checkAccess(childAccountIdentityToBeUpdated, children);
//        verify(loaderChildAccount).load(childAccountIdentityToBeUpdated);
//        verify(childMoneyAccountBeforeUpdate).addMoneyMovement(any(MoneyMovement.class));
//        verify(commandRepository).updateChildMoneyAccount(childMoneyAccountUpdated);
//
//
//    }
//}
