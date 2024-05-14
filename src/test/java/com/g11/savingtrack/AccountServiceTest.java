package com.g11.savingtrack;

import com.g11.savingtrack.dto.response.CustomerResponse;
import com.g11.savingtrack.dto.response.LoginResponse;
import com.g11.savingtrack.entity.Account;
import com.g11.savingtrack.entity.Customer;
import com.g11.savingtrack.exception.account.AccountNotFoundException;
import com.g11.savingtrack.exception.account.UsernamePasswordIncorrectException;
import com.g11.savingtrack.exception.customer.CustomerNotFoundException;
import com.g11.savingtrack.repository.AccountRepository;
import com.g11.savingtrack.repository.CustomerRepository;
import com.g11.savingtrack.security.JwtUtilities;
import com.g11.savingtrack.service.impl.AccountServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AccountRepository userRepository;
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private JwtUtilities jwtUtilities;

    @InjectMocks
    private AccountServiceImpl accountService;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    public void testLogin_Success() {
        // Arrange
        String username = "tuanj";
        String password = "amdin";
        String roleName="user";
        String token="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0dWFuIiwicm9sZSI6WyJ1c2VyIl0sImlhdCI6MTcxNTA5NDkyOSwiZXhwIjoxNzE1MDk4NTI5fQ.45wojGS3bgUzPvbSGZTxR9DGPHuR4_TnS7RTyxgzJoA";

        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getName()).thenReturn(username); // Mock authentication with specific username
        when(authenticationManager.authenticate(any()))
                .thenReturn(mockAuthentication);

        // Mocking the behavior of userRepository
        Account mockUser = new Account();
        mockUser.setUsername(username);
        mockUser.setRole(roleName);
        mockUser.setPassword("$2a$10$nW38Zg65gj.cVhCSljgB6Oglz30l8r1LNdoiQmLhLcsGd9wPXXH7.");
        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(mockUser));
        List<String> roles = new ArrayList<>();
        roles.add(roleName);
        when(jwtUtilities.generateToken(username, roles))
                .thenReturn(token);
        LoginResponse response = accountService.login(username, password);
        assertNotNull(response);
        assertEquals(token, response.getToken());
    }
    @Test
    public void testLogin_UserNotFound() {
        // Mock UserRepository response
        String username = "nonexistent";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Perform login (should throw UsernameNotFoundException)
        assertThrows(UsernameNotFoundException.class, () -> {
            accountService.login(username, "password");
        });
    }
    @Test
    public void testLogin_InvalidCredentials() {
        Account mockUser = new Account();
        String username = "tuan";
        mockUser.setUsername(username);
        mockUser.setPassword("amdin");
        mockUser.setRole("ROLE_USER");
        // Mock UserRepository response


        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        // Mock Authentication failure
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Perform login (should throw UsernamePasswordIncorrectException)
        assertThrows(UsernamePasswordIncorrectException.class, () -> {
            accountService.login(username, "password");
        });
    }


// Test Hàm khác:
    @Test
    public void testCustomerInfor_AccountNotFound() {
        when(authentication.getName()).thenReturn("user");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            accountService.customerinfor(request);
        });
    }

    @Test
    public void testCustomerInfor_CustomerNotFound() {
        Account account = new Account();
        account.setId(1);
        List<Customer> customers = new ArrayList<>();
        when(authentication.getName()).thenReturn("user");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(account));
        when(customerRepository.findByAccountId(1)).thenReturn(customers);

        assertThrows(CustomerNotFoundException.class, () -> {
            accountService.customerinfor(request);
        });
    }

    @Test
    public void testCustomerInfor_Success() {
        Account account = new Account();
        account.setId(1);

        Customer customer = new Customer();
        customer.setId(1);
        customer.setIdentityCardNumber("123456789");
        customer.setAccount(account);
        customer.setAccountNumber("account123");
        customer.setPin("pin");
        customer.setBalance(1000L);
        customer.setEmail("customer@example.com");
        customer.setPhoneNumber("0987654321");
//        customer.setDob("2000-01-01");
        customer.setFullName("Customer Name");
        customer.setAddress("123 Street");
        customer.setGender("M");
        customer.setCareer("Engineer");
        customer.setIncome(5000);
        List<Customer > customers = new ArrayList<>();
        customers.add(customer);
        when(authentication.getName()).thenReturn("user");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(account));
        when(customerRepository.findByAccountId(account.getId())).thenReturn(customers);

        CustomerResponse response = accountService.customerinfor(request);

        assertEquals(customer.getId(), response.getId());
        assertEquals(customer.getIdentityCardNumber(), response.getIdentityCardNumber());
        assertEquals(customer.getAccount(), response.getAccount());
        assertEquals(customer.getAccountNumber(), response.getAccountNumber());
        assertEquals(customer.getPin(), response.getPin());
        assertEquals(customer.getBalance(), response.getBalance());
        assertEquals(customer.getEmail(), response.getEmail());
        assertEquals(customer.getPhoneNumber(), response.getPhoneNumber());
//        assertEquals(customer.getDob(), response.getDob());
        assertEquals(customer.getFullName(), response.getFullName());
        assertEquals(customer.getAddress(), response.getAddress());
        assertEquals(customer.getGender(), response.getGender());
        assertEquals(customer.getCareer(), response.getCareer());
        assertEquals(customer.getIncome(), response.getIncome());
    }
//


}
