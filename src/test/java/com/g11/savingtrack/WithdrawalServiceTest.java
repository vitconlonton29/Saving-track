package com.g11.savingtrack;

import com.g11.savingtrack.entity.*;
import com.g11.savingtrack.exception.account.AccountNotFoundException;
import com.g11.savingtrack.exception.customer.CustomerNotFoundException;
import com.g11.savingtrack.exception.passbook.PassbookNotFoundException;
import com.g11.savingtrack.repository.*;
import com.g11.savingtrack.dto.request.WithdrawalRequest;
import com.g11.savingtrack.dto.response.WithdrawalResponse;
import com.g11.savingtrack.security.JwtUtilities;
import com.g11.savingtrack.service.EmailService;
import com.g11.savingtrack.service.OtpService;
import com.g11.savingtrack.service.impl.EmailServiceImpl;
import com.g11.savingtrack.service.impl.OtpServiceImpl;
import com.g11.savingtrack.service.impl.WithdrawalServiceImpl;
import com.g11.savingtrack.utils.EmailUtils;
import com.g11.savingtrack.utils.ShortTokenReceipt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WithdrawalServiceTest {

    @Mock
    private PassbookRepository passbookRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OtpServiceImpl otpService;

    @Mock
    private EmailServiceImpl emailService;

    @Mock
    private JwtUtilities jwtUtilities;
    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private WithdrawalServiceImpl withdrawalService;
    private Account mockAccount;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    public void testWithdraw_Success() {
        mockAccount = new Account();
        mockAccount.setId(1);
        mockAccount.setUsername("testUser");

        Authentication authentication = new UsernamePasswordAuthenticationToken(mockAccount, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Arrange
        WithdrawalRequest request = new WithdrawalRequest();
        request.setPassbookId(1);
        request.setAmount(1000);
        request.setAll(false);

        Passbook mockPassbook = new Passbook();
        mockPassbook.setId(1);


        Customer mockCustomer = new Customer();
        mockCustomer.setEmail("test@example.com");

        Otp mockOtp = new Otp();
        mockOtp.setId(1);

        when(passbookRepository.findById(request.getPassbookId())).thenReturn(Optional.of(mockPassbook));
        when(accountRepository.findByUsername(anyString())).thenReturn(Optional.of(mockAccount));
        when(customerRepository.findByAccountId(anyInt())).thenReturn(Collections.singletonList(mockCustomer));

        Random mockRandom = mock(Random.class);
        when(mockRandom.nextInt(900000) + 100000).thenReturn(123456);

        when(emailService.sendSimpleMail(any(EmailUtils.class))).thenReturn(true);

        when(otpService.saveOtp(any(Otp.class))).thenReturn(mockOtp);

        when(jwtUtilities.generateTokenShort(anyString(), any(ShortTokenReceipt.class))).thenReturn("mockedToken");

        // Act
        WithdrawalResponse response = withdrawalService.withDraw(request);

        // Assert
        assertNotNull(response);
        assertEquals("mockedToken", response.getShortTokenRecip());
    }

    @Test
    public void testProcessWithdrawal_PassbookNotFound() {
        WithdrawalRequest request=new WithdrawalRequest();
        request.setPassbookId(1);
        request.setAmount(1000);
        request.setAll(false);;

        when(passbookRepository.findById(request.getPassbookId())).thenReturn(Optional.empty());

        assertThrows(PassbookNotFoundException.class, () -> {
            withdrawalService.withDraw(request);
        });
    }

    @Test
    public void testProcessWithdrawal_AccountNotFound() {
        WithdrawalRequest request=new WithdrawalRequest();
        request.setPassbookId(1);
        request.setAmount(1000);
        request.setAll(false);
        Passbook passbook = new Passbook();
        passbook.setId(1);
        when(passbookRepository.findById(request.getPassbookId())).thenReturn(Optional.of(passbook));
        when(accountRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            withdrawalService.withDraw(request);
        });
    }

    @Test
    public void testProcessWithdrawal_CustomerNotFound() {
        WithdrawalRequest request=new WithdrawalRequest();
        request.setPassbookId(1);
        request.setAmount(1000);
        request.setAll(false);
        Passbook passbook = new Passbook();
        passbook.setId(1);
        Account   account = new Account();
        account.setId(1);
        account.setUsername("user");
        Customer customer = new Customer();
        customer.setId(1);
        customer.setEmail("customer@example.com");
        when(passbookRepository.findById(request.getPassbookId())).thenReturn(Optional.of(passbook));
        when(authentication.getName()).thenReturn("user");
        when(accountRepository.findByUsername(any(String.class))).thenReturn(Optional.of(account));
        when(customerRepository.findByAccountId(account.getId())).thenReturn(Collections.emptyList());

        assertThrows(CustomerNotFoundException.class, () -> {
            withdrawalService.withDraw(request);
        });
    }

}

