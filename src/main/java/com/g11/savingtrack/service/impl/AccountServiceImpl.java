package com.g11.savingtrack.service.impl;

import com.g11.savingtrack.dto.request.RegisterRequest;
import com.g11.savingtrack.dto.response.CustomerResponse;
import com.g11.savingtrack.dto.response.LoginResponse;
import com.g11.savingtrack.dto.response.RegisterResponse;
import com.g11.savingtrack.entity.Account;
import com.g11.savingtrack.entity.Customer;
import com.g11.savingtrack.entity.Otp;
import com.g11.savingtrack.exception.account.AccountNotFoundException;
import com.g11.savingtrack.exception.account.UsernamePasswordIncorrectException;
import com.g11.savingtrack.exception.customer.CustomerAlreadyExistException;
import com.g11.savingtrack.exception.customer.CustomerBadRequestException;
import com.g11.savingtrack.exception.customer.CustomerNotFoundException;
import com.g11.savingtrack.repository.AccountRepository;
import com.g11.savingtrack.repository.CustomerRepository;
import com.g11.savingtrack.security.JwtUtilities;
import com.g11.savingtrack.service.AccountService;
import com.g11.savingtrack.service.EmailService;
import com.g11.savingtrack.service.OtpService;
import com.g11.savingtrack.utils.EmailUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service

public class AccountServiceImpl implements AccountService {
  @Autowired
  private EmailService emailService;
  @Autowired
  private OtpService otpService;
  @Autowired
  private CustomerRepository customerRepository;
  @Autowired
  private AccountRepository userRepository;
  @Autowired
  private final AuthenticationManager authenticationManager;
  @Autowired
  private final AccountRepository iUserRepository;
  @Autowired
  private final JwtUtilities jwtUtilities;

  public AccountServiceImpl(AccountRepository userRepository, AuthenticationManager authenticationManager, AccountRepository iUserRepository, JwtUtilities jwtUtilities) {
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
      Account user = iUserRepository.findByUsername(authentication.getName())
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
      throw new UsernamePasswordIncorrectException();
    } catch (Exception ex) {
      // Trường hợp lỗi ngoại lệ khác
      System.out.println("ạih");
      throw new RuntimeException();
    }
  }

  @Override
  public RegisterResponse register(RegisterRequest registerRequest){
    Customer customer= customerRepository.findByIdentityCardNumber(registerRequest.getIdentityCardNumber()).orElseThrow(() -> new CustomerNotFoundException());
    if(customer.getAccount()!=null) throw new CustomerAlreadyExistException();
    if (!customer.getAccountNumber().equals(registerRequest.getAccountNumber())) {
      throw new CustomerBadRequestException("account number is conflic");
    }else if(!customer.getPhoneNumber().equals(registerRequest.getPhoneNumber())){
      throw  new CustomerBadRequestException("phong number is conflic");
    }
    Random random = new Random();
    int rad= random.nextInt(900000) + 100000;
    EmailUtils emailUtils = new EmailUtils();
    emailUtils.setSubject(customer.getEmail());
    emailUtils.setRecipient(customer.getEmail());
    emailUtils.setMsgBody("Đội ơn bạn đã sử  dụng dịch vụ của chúng tôi mã xác nhận của bạn là: "+String.valueOf(rad));
    Boolean statusSendMail = emailService.sendSimpleMail(emailUtils);
    Otp otp = new Otp();
    otp.setCustomer(customer);
    otp.setCode(String.valueOf(rad));
    otp.setDateTimeCreate(LocalDateTime.now());
    otpService.saveOtp(otp);
    if(!statusSendMail){
      throw  new CustomerNotFoundException();
    }
    return new RegisterResponse("success");
  }

  @Override
  public CustomerResponse customerinfor(HttpServletRequest request){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    Account account=userRepository.findByUsername(username).orElseThrow(AccountNotFoundException::new);
    List<Customer> customerList = customerRepository.findByAccountId(account.getId());
    if(customerList.size()==0) throw  new CustomerNotFoundException();
    Customer customer=customerList.get(0);
    return  new CustomerResponse(customer.getId(),customer.getIdentityCardNumber(),customer.getAccount(),customer.getAccountNumber(),customer.getPin(),customer.getBalance(),customer.getEmail(),customer.getPhoneNumber(),customer.getDob(),customer.getFullName(),customer.getAddress(),customer.getGender(),customer.getCareer(),customer.getIncome());
  }

}
