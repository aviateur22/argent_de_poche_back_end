//package com.ctoutweb.argentDePoche.application.helper;
//
//import com.ctoutweb.argentDePoche.application.command.helper.LoaderChildAccount;
//import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccount;
//import com.ctoutweb.argentDePoche.core.domain.childAccount.aggregate.ChildMoneyAccountIdentity;
//import com.ctoutweb.argentDePoche.application.repository.CommandRepository;
//import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.calendar.PeriodSubscription;
//import com.ctoutweb.argentDePoche.core.domain.childAccount.valueObject.calendar.SubscriptionCalendar;
//import com.ctoutweb.argentDePoche.core.domain.exception.ChildMoneyAccountException;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDate;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//public class LoaderChildAccountTest {
//    @Mock
//    CommandRepository commandRepository;
//
//    LoaderChildAccount loaderChildAccount;
//
//    @BeforeEach
//    void init() {
//        MockitoAnnotations.openMocks(this);
//        loaderChildAccount = new LoaderChildAccount(commandRepository);
//    }
//
//    @Test
//    void load_should_throw_when_no_child_account() {
//        /**
//         * Given
//         */
//        when(commandRepository.loadChildMoneyAccountAggregate(
//                any(ChildMoneyAccountIdentity.class),
//                any(LocalDate.class),
//                any(LocalDate.class)))
//                .thenReturn(Optional.empty());
//
//        /**
//         * when
//         */
//        Exception exception = Assertions.assertThrows(ChildMoneyAccountException.class, () -> loaderChildAccount.load(mock(ChildMoneyAccountIdentity.class)));
//
//    }
//
//    @Test
//    void load_should_return_ChildMoneyAccount_weekly() {
//        /**
//         * Given
//         */
//        ChildMoneyAccount monthAccount = mock(ChildMoneyAccount.class);
//        ChildMoneyAccount weekAccount = mock(ChildMoneyAccount.class);
//        SubscriptionCalendar calendar = mock(SubscriptionCalendar.class);
//
//        when(monthAccount.getCalendarSubscription())
//                .thenReturn(calendar);
//
//        when(calendar.periodSubscription())
//                .thenReturn(PeriodSubscription.WEEK);
//
//        when(commandRepository.loadChildMoneyAccountAggregate(any(ChildMoneyAccountIdentity.class), any(LocalDate.class), any(LocalDate.class)))
//                .thenReturn(Optional.of(monthAccount));
//
//        when(monthAccount.loadWeekPeriodSubscription())
//                .thenReturn(weekAccount);
//
//        /**
//         * when
//         */
//       var childMoneyAccountResult = loaderChildAccount.load(mock(ChildMoneyAccountIdentity.class));
//
//        /**
//         * Then
//         */
//        verify(monthAccount).loadWeekPeriodSubscription();
//        assertNotNull(childMoneyAccountResult);
//        assertEquals(weekAccount, childMoneyAccountResult);
//
//    }
//
//    @Test
//    void load_should_return_ChildMoneyAccount_monthly() {
//        /**
//         * Given
//         */
//        ChildMoneyAccount monthAccount = mock(ChildMoneyAccount.class);
//        SubscriptionCalendar calendar = mock(SubscriptionCalendar.class);
//
//        when(monthAccount.getCalendarSubscription())
//                .thenReturn(calendar);
//
//        when(calendar.periodSubscription())
//                .thenReturn(PeriodSubscription.MONTH);
//
//        when(commandRepository.loadChildMoneyAccountAggregate(
//                any(ChildMoneyAccountIdentity.class),
//                any(LocalDate.class),
//                any(LocalDate.class)))
//                .thenReturn(Optional.of(monthAccount));
//
//        /**
//         * when
//         */
//        var childMoneyAccountResult = loaderChildAccount.load(mock(ChildMoneyAccountIdentity.class));
//
//        /**
//         * Then
//         */
//        verify(monthAccount, never()).loadWeekPeriodSubscription();
//        assertNotNull(childMoneyAccountResult);
//        assertEquals(monthAccount, childMoneyAccountResult);
//
//    }
//}
