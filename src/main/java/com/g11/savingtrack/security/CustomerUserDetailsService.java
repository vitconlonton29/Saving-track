package com.g11.savingtrack.security;

import com.g11.savingtrack.entity.Employee;
import com.g11.savingtrack.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.*;


@Component
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository ;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Employee> optionalEmployee = employeeRepository.findByUsername(username);
        Employee employee = optionalEmployee.orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        // Tạo danh sách quyền từ role của Employee
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(employee.getRole()));

        // Tạo UserDetails từ thông tin Employee
        return User.withUsername(employee.getUsername())
                .password(employee.getPassword())
                .authorities(authorities)
                .build();

    }


}