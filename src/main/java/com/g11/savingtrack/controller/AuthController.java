package com.g11.savingtrack.controller;

import com.g11.savingtrack.dto.request.LoginRequest;
import com.g11.savingtrack.dto.response.LoginResponse;
import com.g11.savingtrack.service.EmployeeService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class AuthController {

    @Autowired
    private EmployeeService userService;

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws Exception {
        return userService.login(loginRequest.getUsername(), loginRequest.getPassword());
    }
}
