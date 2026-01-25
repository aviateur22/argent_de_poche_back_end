//package com.ctoutweb.argentDePoche.application;
//
//import com.ctoutweb.argentDePoche.application.api.ChildAccountManager;
//import com.ctoutweb.argentDePoche.application.command.dto.command.UpdateInitialChildMoneyCommand;
//import com.ctoutweb.argentDePoche.application.configuration.bus.CommandBus;
//import com.ctoutweb.argentDePoche.application.configuration.bus.EventBus;
//import com.ctoutweb.argentDePoche.application.configuration.bus.QueryBus;
//import com.ctoutweb.argentDePoche.application.command.dto.command.AddMoneyMovementCommand;
//import com.ctoutweb.argentDePoche.application.command.dto.command.UpdateChildImageCommand;
//import com.ctoutweb.argentDePoche.application.port.AddMoneyMovement;
//import com.ctoutweb.argentDePoche.application.port.ImageResource;
//import com.ctoutweb.argentDePoche.application.query.dto.ChildAccountDto;
//import com.ctoutweb.argentDePoche.application.query.dto.query.LoadChildAccountQuery;
//import com.ctoutweb.argentDePoche.application.command.helper.LoaderChildAccount;
//import com.ctoutweb.argentDePoche.application.service.ChildAccountServiceImpl;
//import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccount;
//import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
//import com.ctoutweb.argentDePoche.core.domain.childAccount.entity.child.Child;
//import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.account.ChildMoney;
//import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.calendar.SubscriptionCalendar;
//import com.ctoutweb.argentDePoche.core.domain.exception.ChildImageException;
//import com.ctoutweb.argentDePoche.core.domain.familyAccount.aggregate.FamilyAccountIdentity;
//import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.Parent;
//import com.ctoutweb.argentDePoche.core.domain.familyAccount.entity.parent.ParentIdentity;
//import com.ctoutweb.argentDePoche.application.generateData.GenerateCalendar;
//import com.ctoutweb.argentDePoche.application.generateData.GenerateChild;
//import com.ctoutweb.argentDePoche.application.generateData.GenerateChildMoney;
//import com.ctoutweb.argentDePoche.application.generateData.GenerateParent;
//import com.ctoutweb.argentDePoche.application.spi.RandomProvider;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//
//import java.math.BigDecimal;
//
//public class ChildAccountManagerTest {
//    @Mock
//    EventBus eventBus;
//
//    @Mock
//    CommandBus commandBus;
//
//    @Mock
//    QueryBus queryBus;
//
//    @Mock
//    RandomProvider randomProvider;
//
//    @Mock
//    LoaderChildAccount loaderChildAccount;
//
//    ChildAccountManager childAccountManager;
//
//    @BeforeEach
//    void init() {
//        MockitoAnnotations.openMocks(this);
//
//        childAccountManager = new ChildAccountServiceImpl(
//                eventBus,
//                commandBus,
//                queryBus,
//                randomProvider
//        );
//
//    }
//    @Test
//    void should_load_child_account() {
//        /**
//         * Given
//         */
//        ChildMoneyAccount childAccountInFamily = mockChildAccount();
//        ChildMoneyAccountIdentity childMoneyAccountIdToLoad = childAccountInFamily.getChildMoneyAccountId();
//        ParentIdentity parentIdentity = new ParentIdentity(1L);
//        FamilyAccountIdentity familyAccountIdentity = new FamilyAccountIdentity(1L);
//        ChildAccountDto dto = Mockito.mock(ChildAccountDto.class);
//
//        Mockito.when(queryBus.executeQuery(ArgumentMatchers.any(LoadChildAccountQuery.class)))
//                .thenReturn(dto);
//
//        /**
//         * when
//         */
//        var actualDto = childAccountManager.loadChildAccount(childMoneyAccountIdToLoad, parentIdentity);
//
//        /**
//         * Then
//         */
//        Mockito.verify(queryBus).executeQuery(ArgumentMatchers.any(LoadChildAccountQuery.class));
//        Assertions.assertEquals(dto, actualDto);
//    }
//
//    @Test
//    void should_update_child_image() {
//        /**
//         * Given
//         */
//        ChildMoneyAccountIdentity childAccountId = new ChildMoneyAccountIdentity(1L);
//        ParentIdentity parentIdentity = new ParentIdentity(1L);
//        FamilyAccountIdentity familyAccountIdentity = new FamilyAccountIdentity(1L);
//
//        // Fake de l'image a modifier
//        byte[] imageBytes = "fakeImage".getBytes();
//        ImageResource imageResource = () -> imageBytes;
//
//        // Mock du randomProvider
//        Mockito.when(randomProvider.generateUniqueRandomUuid())
//                .thenReturn("newRandomName");
//
//        Mockito.when(commandBus.executeCommand(ArgumentMatchers.any(UpdateChildImageCommand.class)))
//                .thenReturn(childAccountId);
//
//        /**
//         * when
//         */
//        var actualChildIdentity = childAccountManager.updateChildImage(childAccountId, imageResource, parentIdentity);
//
//        /**
//         * Then
//         */
//        Mockito.verify(commandBus).executeCommand(ArgumentMatchers.any(UpdateChildImageCommand.class));
//        Assertions.assertEquals(childAccountId,actualChildIdentity);
//    }
//
//    @Test
//    void updateChildImage_should_throw_when_image_null() {
//        /**
//         * Given
//         */
//        ChildMoneyAccountIdentity childAccountId = new ChildMoneyAccountIdentity(1L);
//        ParentIdentity parentIdentity = new ParentIdentity(1L);
//        FamilyAccountIdentity familyAccountIdentity = new FamilyAccountIdentity(1L);
//
//        //Resource image à NULL
//        ImageResource imageResource = null;
//
//        /**
//         * when
//         */
//        Exception exception = Assertions.assertThrows(ChildImageException.class,
//                () -> childAccountManager.updateChildImage(childAccountId, imageResource, parentIdentity));
//
//        Assertions.assertEquals("La nouvelle image n'est pas valide", exception.getMessage());
//    }
//
//    @Test
//    void updateChildImage_should_throw_when_image_size_0() {
//        /**
//         * Given
//         */
//        ChildMoneyAccountIdentity childAccountId = new ChildMoneyAccountIdentity(1L);
//        ParentIdentity parentIdentity = new ParentIdentity(1L);
//        FamilyAccountIdentity familyAccountIdentity = new FamilyAccountIdentity(1L);
//
//        //Resource image à NULL
//        ImageResource imageResource = ""::getBytes;
//
//        /**
//         * when
//         */
//        Exception exception = Assertions.assertThrows(ChildImageException.class,
//                () -> childAccountManager.updateChildImage(childAccountId, imageResource, parentIdentity));
//
//        Assertions.assertEquals("La nouvelle image n'est pas valide", exception.getMessage());
//    }
//
//    @Test
//    void should_add_negative_money_movement() {
//        /**
//         * Given
//         */
//        ChildMoneyAccountIdentity childAccountId = new ChildMoneyAccountIdentity(1L);
//        ParentIdentity parentIdentity = new ParentIdentity(1L);
//        FamilyAccountIdentity familyAccountIdentity = new FamilyAccountIdentity(1L);
//
//        AddMoneyMovement addMoneyMovement = new AddMoneyMovement() {
//            @Override
//            public BigDecimal getFluctuationPrice() {
//                return null;
//            }
//
//            @Override
//            public String getMovementActionCode() {
//                return "";
//            }
//
//            @Override
//            public String getMovementReasonCode() {
//                return "";
//            }
//
//            @Override
//            public ParentIdentity getInitiatedByParent() {
//                return null;
//            }
//        };
//
//        Mockito.when(commandBus.executeCommand(ArgumentMatchers.any(AddMoneyMovementCommand.class)))
//                .thenReturn(childAccountId);
//
//        /**
//         * When
//         */
//        var actualChildIdentity = childAccountManager.addChildMoneyMovement(
//                childAccountId,
//                addMoneyMovement,
//                parentIdentity);
//
//        /**
//         * Then
//         */
//        Mockito.verify(commandBus).executeCommand(ArgumentMatchers.any(AddMoneyMovementCommand.class));
//        Assertions.assertEquals(childAccountId, actualChildIdentity);
//
//
//    }
//
//    @Test
//    void should_add_positive_money_movement() {
//        /**
//         * Given
//         */
//        ChildMoneyAccountIdentity childAccountId = new ChildMoneyAccountIdentity(1L);
//        ParentIdentity parentIdentity = new ParentIdentity(1L);
//        FamilyAccountIdentity familyAccountIdentity = new FamilyAccountIdentity(1L);
//
//
//        // Mock l'ajout d'un mouvement d'argent
//        Mockito.when(commandBus.executeCommand(ArgumentMatchers.any(AddMoneyMovementCommand.class)))
//                .thenReturn(childAccountId);
//
//        // Fake un mouvement d'argent nédatif sur le compte
//        AddMoneyMovement addMoneyMovement = new AddMoneyMovement() {
//            @Override
//            public BigDecimal getFluctuationPrice() {
//                return null;
//            }
//
//            @Override
//            public String getMovementActionCode() {
//                return "";
//            }
//
//            @Override
//            public String getMovementReasonCode() {
//                return "";
//            }
//
//            @Override
//            public ParentIdentity getInitiatedByParent() {
//                return null;
//            }
//        };
//
//        /**
//         * When
//         */
//        var actualChildIdentity = childAccountManager.addChildMoneyMovement(
//                childAccountId,
//                addMoneyMovement,
//                parentIdentity);
//
//        /**
//         * Then
//         */
//        Mockito.verify(commandBus).executeCommand(ArgumentMatchers.any(AddMoneyMovementCommand.class));
//        Assertions.assertEquals(childAccountId, actualChildIdentity);
//    }
//
//    @Test
//    void should_update_initial_money_at_period_start() {
//        /**
//         * Given
//         */
//        ChildMoneyAccountIdentity childAccountId = new ChildMoneyAccountIdentity(1L);
//        ParentIdentity parentIdentity = new ParentIdentity(1L);
//        FamilyAccountIdentity familyAccountIdentity = new FamilyAccountIdentity(1L);
//
//
//        // Mock l'ajout d'un mouvement d'argent
//        Mockito.when(commandBus.executeCommand(ArgumentMatchers.any(UpdateInitialChildMoneyCommand.class)))
//                .thenReturn(childAccountId);
//
//        /**
//         * When
//         */
//        var actualChildIdentity = childAccountManager.modulateInitialChildMoney(
//                childAccountId,
//                BigDecimal.valueOf(2),
//                parentIdentity);
//
//        /**
//         * Then
//         */
//        Mockito.verify(commandBus).executeCommand(ArgumentMatchers.any(UpdateInitialChildMoneyCommand.class));
//        Assertions.assertEquals(childAccountId, actualChildIdentity);
//    }
//
//    private ChildMoneyAccount mockChildAccount() {
//        BigDecimal monthlyMovementPrice = BigDecimal.valueOf(1.2);
//        BigDecimal weeklyMovementPrice = BigDecimal.valueOf(1.1);
//        BigDecimal moneyAtPeriodStart = BigDecimal.valueOf(2);
//
//        // Le mouvement d'argent sur la semaine doit être positive
//        Assertions.assertFalse(weeklyMovementPrice.compareTo(BigDecimal.ZERO) < 0, "Le mouvement d'argent sur la semaine doit être positif");
//
//        ChildMoneyAccountIdentity childMoneyAccountId = new ChildMoneyAccountIdentity(2L);
//        Child child = new GenerateChild().generate();
//        Parent parent = new GenerateParent().generate();
//        SubscriptionCalendar monthlyCalendar = new GenerateCalendar().generate();
//        ChildMoney monthlyChildMoney = new GenerateChildMoney(parent).generateMonthly(moneyAtPeriodStart, monthlyMovementPrice, weeklyMovementPrice);
//
//        return new ChildMoneyAccount(childMoneyAccountId, child, monthlyCalendar, monthlyChildMoney);
//    }
//}
