package com.g11.savingtrack.service;

import com.g11.savingtrack.dto.response.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeService {
    LoginResponse login(String username, String password) ;
}
