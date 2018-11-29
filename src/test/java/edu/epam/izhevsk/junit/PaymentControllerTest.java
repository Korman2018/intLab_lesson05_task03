package edu.epam.izhevsk.junit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalMatchers.geq;
import static org.mockito.AdditionalMatchers.lt;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

public class PaymentControllerTest {
    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private AccountService accountServiceMock;

    @Mock
    private DepositService depositServiceMock;

    @BeforeEach
    void init() throws InsufficientFundsException {
        initMocks(this);

        when(accountServiceMock.isUserAuthenticated(eq(100L))).thenReturn(true);
        when(depositServiceMock.deposit(geq(100L), anyLong())).thenThrow(InsufficientFundsException.class);
        when(depositServiceMock.deposit(lt(100L), anyLong())).thenReturn("successful");
    }

    @Test
    void testIsAuthenticatedOneTime() throws InsufficientFundsException {
        paymentController.deposit(50L, 100L);
        verify(accountServiceMock, times(1)).isUserAuthenticated(eq(100L));
    }

    @Test
    void testNotAuthenticatedUser() {
        SecurityException securityException = assertThrows(SecurityException.class,
                () -> paymentController.deposit(90L, 200L));
        assertEquals("User not authenticated: 200", securityException.getMessage());
    }

    @Test
    void testAmountIsToBig() {
        assertThrows(InsufficientFundsException.class, () -> paymentController.deposit(900L, 100L));
    }

    @Test
    void testIfDepositLessThanHundred() throws InsufficientFundsException {
        assertEquals("successful",depositServiceMock.deposit(10L,10L));
    }
}
