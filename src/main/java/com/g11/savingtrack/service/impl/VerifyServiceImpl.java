package com.g11.savingtrack.service.impl;

import com.g11.savingtrack.dto.request.VerifyRequest;
import com.g11.savingtrack.dto.response.VerifyResponse;
import com.g11.savingtrack.entity.Account;
import com.g11.savingtrack.entity.Customer;
import com.g11.savingtrack.entity.Otp;
import com.g11.savingtrack.exception.Otp.OtpNotFoundException;
import com.g11.savingtrack.exception.customer.CustomerAlreadyExistException;
import com.g11.savingtrack.exception.customer.CustomerNotFoundException;
import com.g11.savingtrack.repository.AccountRepository;
import com.g11.savingtrack.repository.CustomerRepository;
import com.g11.savingtrack.service.AccountService;
import com.g11.savingtrack.service.OtpService;
import com.g11.savingtrack.service.VerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VerifyServiceImpl implements VerifyService {
    @Autowired
    public AccountRepository accountRepository;
    @Autowired
    public   OtpService otpService;
    @Autowired
    public CustomerRepository customerRepository;
    @Override
    public VerifyResponse verify(VerifyRequest verifyRequest){
        Customer customer= customerRepository.findByIdentityCardNumber(verifyRequest.getIdentityCardNumber()).orElseThrow(() -> new CustomerNotFoundException());
        if(customer.getAccount()==null) throw new CustomerAlreadyExistException();
        List<Otp> otpList=otpService.findLatestOtpByCustomerId(customer.getId());
        if(otpList.size()==0){
            throw new OtpNotFoundException();
        }
        LocalDateTime dateTime1 = LocalDateTime.now();
        LocalDateTime dateTime2 = otpList.get(0).getDateTimeCreate();
        Duration duration = Duration.between(dateTime1, dateTime2);
        long seconds = Math.abs(duration.getSeconds());
        if(seconds>30 ||!otpList.get(0).getCode().equals(verifyRequest.getCode())){
            throw  new OtpNotFoundException();
        }
        Account newAccount = new Account();
        newAccount.setRole("user");
        newAccount.setUsername(verifyRequest.username);
        newAccount.setPassword(new BCryptPasswordEncoder().encode(verifyRequest.getPassword()));
        accountRepository.save(newAccount);
        return new VerifyResponse("success");
    }

}
