package com.g11.savingtrack.service;

import com.g11.savingtrack.dtos.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeService {
    ResponseEntity<LoginResponse> login(String username, String password) throws Exception;
}
