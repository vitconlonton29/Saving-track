package com.g11.savingtrack.service;

import com.g11.savingtrack.dto.request.RegisterRequest;
import com.g11.savingtrack.dto.response.LoginResponse;
import com.g11.savingtrack.dto.response.RegisterResponse;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {
    LoginResponse login(String username, String password) ;

    RegisterResponse register(RegisterRequest registerRequest);
}
