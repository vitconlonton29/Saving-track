package com.g11.savingtrack;

import com.g11.savingtrack.dto.response.LoginResponse;
import com.g11.savingtrack.entity.Account;
import com.g11.savingtrack.exception.account.UsernamePasswordIncorrectException;
import com.g11.savingtrack.repository.AccountRepository;
import com.g11.savingtrack.security.JwtUtilities;
import com.g11.savingtrack.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
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
    private JwtUtilities jwtUtilities;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
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
        // Arrange
        String username = "nonExistingUser";
        String password = "testPassword";
        Authentication mockAuthentication = mock(Authentication.class);
//        when(mockAuthentication.getName()).thenReturn(username); // Mock authentication with specific username
        when(authenticationManager.authenticate(any()))
                .thenReturn(null);
        // Mocking the behavior of userRepository to return empty
        when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RuntimeException.class, () -> {
            accountService.login(username, password);
        });
    }

}
