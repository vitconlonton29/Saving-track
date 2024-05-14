package com.g11.savingtrack;

import com.g11.savingtrack.dto.request.VerifyRequest;
import com.g11.savingtrack.dto.response.VerifyResponse;
import com.g11.savingtrack.entity.Account;
import com.g11.savingtrack.entity.Customer;
import com.g11.savingtrack.entity.Otp;
import com.g11.savingtrack.exception.Otp.OtpNotFoundException;
import com.g11.savingtrack.exception.customer.CustomerAlreadyExistException;
import com.g11.savingtrack.exception.customer.CustomerNotFoundException;
import com.g11.savingtrack.repository.AccountRepository;
import com.g11.savingtrack.repository.CustomerRepository;
import com.g11.savingtrack.repository.OtpRepository;
import com.g11.savingtrack.service.impl.OtpServiceImpl;
import com.g11.savingtrack.service.impl.VerifyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VerifyServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private OtpServiceImpl otpService;

    @Mock
    private OtpRepository otpRepository;

    @InjectMocks
    private VerifyServiceImpl verifyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testVerify_CustomerNotFound() {
        VerifyRequest verifyRequest = new VerifyRequest();
        verifyRequest.setIdentityCardNumber("123456789");
        verifyRequest.setUsername("newuser");
        verifyRequest.setPassword("password");
        verifyRequest.setCode("123456");
        when(customerRepository.findByIdentityCardNumber(any(String.class))).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> {
            verifyService.verify(verifyRequest);
        });
    }

    @Test
    public void testVerify_CustomerAlreadyHasAccount() {
        // Arrange
        VerifyRequest request = new VerifyRequest();
        request.setIdentityCardNumber("123456789");

        Customer mockCustomer = new Customer();
        mockCustomer.setAccount(new Account()); // Customer already has an account

        when(customerRepository.findByIdentityCardNumber("123456789")).thenReturn(Optional.of(mockCustomer));

        // Act & Assert
        assertThrows(CustomerAlreadyExistException.class, () -> verifyService.verify(request));

    }



    @Test
    public void testVerify_Success() {
        Customer mockCustomer = new Customer();
        mockCustomer.setId(1);
        mockCustomer.setEmail("d");
        mockCustomer.setIdentityCardNumber("123456789");
        mockCustomer.setPhoneNumber("0379230864");
        mockCustomer.setAccount(null);
        // Arrange
        VerifyRequest request = new VerifyRequest();
        request.setIdentityCardNumber("123456789");
        request.setUsername("newUser");
        request.setPassword("password");
        request.setCode("123456");
        Otp mockOtp = new Otp();
        mockOtp.setId(1);
        mockOtp.setCode("123456");
        mockOtp.setDateTimeCreate(LocalDateTime.now().minusSeconds(300)); // Set OTP within the time limit

        when(customerRepository.findByIdentityCardNumber("123456789")).thenReturn(Optional.of(mockCustomer));
        when(otpService.findLatestOtpByCustomerId(mockCustomer.getId()))
                .thenReturn(Collections.singletonList(mockOtp));
        VerifyResponse response = verifyService.verify(request);
        assertNotNull(response);
        assertEquals("success", response.getMes());
    }


    @Test
    public void testVerify_OtpExpired() {
        // Arrange
        VerifyRequest request = new VerifyRequest();
        request.setIdentityCardNumber("123456789");

        Customer mockCustomer = new Customer();
        mockCustomer.setIdentityCardNumber(("123456789"));
        Otp mockOtp = new Otp();
        mockOtp.setCode("123456");
        mockOtp.setDateTimeCreate(LocalDateTime.now().minusSeconds(600)); // OTP expired

        when(customerRepository.findByIdentityCardNumber("123456789")).thenReturn(Optional.of(mockCustomer));
        when(otpService.findLatestOtpByCustomerId(mockCustomer.getId()))
                .thenReturn(Collections.singletonList(mockOtp));

        // Act & Assert
        assertThrows(OtpNotFoundException.class, () -> verifyService.verify(request));

    }
}

