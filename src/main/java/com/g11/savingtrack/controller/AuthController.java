package com.g11.savingtrack.controller;

import com.g11.savingtrack.dto.ResponseGeneral;
import com.g11.savingtrack.dto.request.LoginRequest;
import com.g11.savingtrack.dto.request.RegisterRequest;
import com.g11.savingtrack.dto.request.VerifyRequest;
import com.g11.savingtrack.dto.response.LoginResponse;
import com.g11.savingtrack.dto.response.RegisterResponse;
import com.g11.savingtrack.dto.response.VerifyResponse;
import com.g11.savingtrack.service.AccountService;
import com.g11.savingtrack.service.VerifyService;
import org.springframework.security.config.authentication.PasswordEncoderParser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.g11.savingtrack.constants.PassbookManagerConstants.MessageCode.SUCCESS;

@RestController
@RequestMapping("/api/saving")
public class AuthController {

    @Autowired
    private AccountService userService;
    @Autowired
    private VerifyService verifyService;

    @PostMapping("login")
    public ResponseGeneral<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws Exception {
//        System.out.println(new BCryptPasswordEncoder().encode("amdin"));
        return ResponseGeneral.ofCreated(
                SUCCESS,
                userService.login(loginRequest.getUsername(), loginRequest.getPassword())
        );
    }
    @PostMapping("register")
    public ResponseGeneral<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {

        return ResponseGeneral.ofCreated(
                SUCCESS,
                userService.register(registerRequest)
        );
    }
    @PostMapping("verify")
    public ResponseGeneral<VerifyResponse> verify(@RequestBody VerifyRequest verifyRequest) {

        return ResponseGeneral.ofCreated(
                SUCCESS,
                verifyService.verify(verifyRequest)
        );
    }
}
