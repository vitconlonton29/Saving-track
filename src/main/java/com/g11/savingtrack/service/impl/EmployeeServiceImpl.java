package com.g11.savingtrack.service.impl;

import com.g11.savingtrack.dtos.LoginResponse;
import com.g11.savingtrack.entity.Employee;
import com.g11.savingtrack.repository.EmployeeRepository;
import com.g11.savingtrack.security.JwtUtilities;
import com.g11.savingtrack.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository userRepository;
    @Autowired
    private final AuthenticationManager authenticationManager ;
    @Autowired
    private final EmployeeRepository iUserRepository ;
    @Autowired
    private final JwtUtilities jwtUtilities ;


    public EmployeeServiceImpl(EmployeeRepository userRepository, AuthenticationManager authenticationManager, EmployeeRepository iUserRepository, JwtUtilities jwtUtilities) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.iUserRepository = iUserRepository;
        this.jwtUtilities = jwtUtilities;

    }

    @Override
    public ResponseEntity<LoginResponse> login(String username, String password) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Employee user = iUserRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            List<String> rolesNames = new ArrayList<>();
            rolesNames.add(user.getRole());
            String token = jwtUtilities.generateToken(user.getUsername(), rolesNames);
            return new ResponseEntity<>(new LoginResponse(token), HttpStatus.CREATED);
        }  catch (AuthenticationException ex) {
            return new ResponseEntity<>(new LoginResponse("Username or Password correct"), HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            return new ResponseEntity<>(new LoginResponse("Error Server"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
