package com.g11.savingtrack.controller;

import com.g11.savingtrack.dto.ResponseGeneral;
import com.g11.savingtrack.dto.request.LoginRequest;
import com.g11.savingtrack.dto.response.LoginResponse;
import com.g11.savingtrack.service.EmployeeService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.g11.savingtrack.constants.PassbookManagerConstants.MessageCode.SUCCESS;

@RestController
@RequestMapping("/api/v1/")
public class AuthController {

    @Autowired
    private EmployeeService userService;

    @PostMapping("login")
    public ResponseGeneral<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws Exception {
        return ResponseGeneral.ofCreated(
                SUCCESS,
                userService.login(loginRequest.getUsername(), loginRequest.getPassword())
        );

    }
}
