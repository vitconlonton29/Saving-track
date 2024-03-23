package com.g11.savingtrack.service.impl;

import com.g11.savingtrack.dto.response.LoginResponse;
import com.g11.savingtrack.entity.Account;
import com.g11.savingtrack.exception.account.UsernamePasswordIncorrectException;
import com.g11.savingtrack.repository.AccountRepository;
import com.g11.savingtrack.security.JwtUtilities;
import com.g11.savingtrack.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service

public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository userRepository;
    @Autowired
    private final AuthenticationManager authenticationManager ;
    @Autowired
    private final AccountRepository iUserRepository ;
    @Autowired
    private final JwtUtilities jwtUtilities ;


    public AccountServiceImpl(AccountRepository userRepository, AuthenticationManager authenticationManager, AccountRepository iUserRepository, JwtUtilities jwtUtilities) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.iUserRepository = iUserRepository;
        this.jwtUtilities = jwtUtilities;

    }

    @Override
    public LoginResponse login(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Account user = iUserRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            List<String> rolesNames = new ArrayList<>();
            rolesNames.add(user.getRole());
            String token = jwtUtilities.generateToken(user.getAccountNumber(), rolesNames);
            return new LoginResponse(token);
        } catch (UsernameNotFoundException ex) {
            // Trường hợp không tìm thấy người dùng
            throw ex; // Re-throw ngoại lệ để xử lý ở nơi gọi
        } catch (AuthenticationException ex) {
            // Trường hợp sai tên người dùng hoặc mật khẩu
            throw new UsernamePasswordIncorrectException();
        } catch (Exception ex) {
            // Trường hợp lỗi ngoại lệ khác
            System.out.println("ạih");
            throw new RuntimeException();
        }
    }
}
