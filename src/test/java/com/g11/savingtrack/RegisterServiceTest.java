package com.g11.savingtrack;
import com.g11.savingtrack.dto.request.RegisterRequest;
import com.g11.savingtrack.dto.response.RegisterResponse;
import com.g11.savingtrack.entity.Account;
import com.g11.savingtrack.entity.Customer;
import com.g11.savingtrack.entity.Otp;
import com.g11.savingtrack.exception.customer.CustomerAlreadyExistException;
import com.g11.savingtrack.repository.CustomerRepository;
import com.g11.savingtrack.service.AccountService;
import com.g11.savingtrack.service.EmailService;
import com.g11.savingtrack.service.OtpService;
import com.g11.savingtrack.service.impl.AccountServiceImpl;
import com.g11.savingtrack.service.impl.EmailServiceImpl;
import com.g11.savingtrack.utils.EmailUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class RegisterServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private OtpService otpService;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegisterSuccess() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setIdentityCardNumber("123456789");
        registerRequest.setAccountNumber("123456789");
        registerRequest.setPhoneNumber("123456789");
        registerRequest.setPin("1234");

        Customer customer = new Customer();
        customer.setIdentityCardNumber("123456789");
        customer.setAccount(null);
        customer.setAccountNumber("123456789");
        customer.setPhoneNumber("123456789");
        customer.setEmail("test@example.com");

        when(customerRepository.findByIdentityCardNumber("123456789")).thenReturn(Optional.of(customer));
        when(emailService.sendSimpleMail(any(EmailUtils.class))).thenReturn(true);

        RegisterResponse response = accountService.register(registerRequest);

        assertEquals("success", response.getMes());
        verify(customerRepository, times(1)).findByIdentityCardNumber("123456789");
        verify(emailService, times(1)).sendSimpleMail(any(EmailUtils.class));
        verify(otpService, times(1)).saveOtp(any(Otp.class));
    }

    @Test(expected = CustomerAlreadyExistException.class)
    public void testRegisterWithExistingAccount() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setIdentityCardNumber("123456789");
        registerRequest.setAccountNumber("123456789");
        registerRequest.setPhoneNumber("123456789");
        registerRequest.setPin("1234");

        Customer customer = new Customer();
        customer.setIdentityCardNumber("123456789");
        customer.setAccount(new Account()); // Assume account exists
        customer.setAccountNumber("123456789");
        customer.setPhoneNumber("123456789");
        customer.setEmail("test@example.com");

        when(customerRepository.findByIdentityCardNumber("123456789")).thenReturn(Optional.of(customer));

        accountService.register(registerRequest);
    }
}
