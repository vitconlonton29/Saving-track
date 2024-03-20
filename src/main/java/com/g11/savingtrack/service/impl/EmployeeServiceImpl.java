package com.g11.savingtrack.service.impl;

import com.g11.savingtrack.dto.response.LoginResponse;
import com.g11.savingtrack.entity.Employee;
import com.g11.savingtrack.exception.customer.CustomerNotFoundException;
import com.g11.savingtrack.exception.employee.EmployeeNotFoundException;
import com.g11.savingtrack.exception.employee.UsernamePasswordException;
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
    public LoginResponse login(String username, String password) {
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
            return new LoginResponse(token);
        } catch (UsernameNotFoundException ex) {
            // Trường hợp không tìm thấy người dùng
            throw ex; // Re-throw ngoại lệ để xử lý ở nơi gọi
        } catch (AuthenticationException ex) {
            // Trường hợp sai tên người dùng hoặc mật khẩu
            throw new UsernamePasswordException();
        } catch (Exception ex) {
            // Trường hợp lỗi ngoại lệ khác
            System.out.println("ạih");
            throw new RuntimeException();
        }
    }
}
