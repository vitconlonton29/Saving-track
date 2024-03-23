package com.g11.savingtrack.security;

import com.g11.savingtrack.entity.Account;
import com.g11.savingtrack.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.*;


@Component
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> optionalEmployee = accountRepository.findByUsername(username);
        Account account = optionalEmployee.orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        // Tạo danh sách quyền từ role của Employee
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(account.getRole()));

        // Tạo UserDetails từ thông tin Employee
        return User.withUsername(account.getAccountNumber())
                .password(account.getPassword())
                .authorities(authorities)
                .build();

    }


}
