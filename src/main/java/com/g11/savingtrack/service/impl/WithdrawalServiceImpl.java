package com.g11.savingtrack.service.impl;

import com.g11.savingtrack.dto.request.WithdrawalRequest;
import com.g11.savingtrack.dto.response.WithdrawalResponse;
import com.g11.savingtrack.entity.*;
import com.g11.savingtrack.exception.account.AccountNotFoundException;
import com.g11.savingtrack.exception.customer.CustomerNotFoundException;
import com.g11.savingtrack.exception.passbook.PassbookNotFoundException;
import com.g11.savingtrack.repository.AccountRepository;
import com.g11.savingtrack.repository.CustomerRepository;
import com.g11.savingtrack.repository.PassbookRepository;
import com.g11.savingtrack.repository.ReceiptRepository;
import com.g11.savingtrack.security.JwtUtilities;
import com.g11.savingtrack.service.EmailService;
import com.g11.savingtrack.service.OtpService;
import com.g11.savingtrack.service.WithdrawalService;
import com.g11.savingtrack.utils.EmailUtils;
import com.g11.savingtrack.utils.ShortTokenReceipt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class WithdrawalServiceImpl implements WithdrawalService {
  private final ReceiptRepository receiptRepository;
  private final CustomerRepository customerRepository;
  private final PassbookRepository passbookRepository;
  private final OtpService otpService;
  private final EmailService emailService;
  private final  JwtUtilities jwtUtilities;
  private final AccountRepository accountRepository;



  @Override
  public WithdrawalResponse withDraw(WithdrawalRequest request) {
    log.info("(withDraw) request:{}", request);

    Passbook passbook = passbookRepository.findById(request.getPassbookId()).orElseThrow(PassbookNotFoundException::new);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    Account account=accountRepository.findByUsername(username).orElseThrow(AccountNotFoundException::new);
    List<Customer> customerList = customerRepository.findByAccountId(account.getId());
    if(customerList.size()==0) throw  new CustomerNotFoundException();
    Customer customer=customerList.get(0);
    EmailUtils emailUtils= new EmailUtils();
    emailUtils.setSubject("verify rut tien");
    Random random = new Random();
    int rad= random.nextInt(900000) + 100000;
    emailUtils.setMsgBody("ma xac thuc cua bn la "+String.valueOf(rad));
    emailUtils.setRecipient(customer.getEmail());
    emailService.sendSimpleMail(emailUtils);
    Otp otp = new Otp();
    otp.setCustomer(customer);
    otp.setCode(String.valueOf(rad));
    otp.setDateTimeCreate(LocalDateTime.now());
    Otp otpNew= otpService.saveOtp(otp);

//
//
//    long amountWithdraw;
//    long amount = passbook.getAmount();
//    long balance = customer.getBalance();
//
//    if (request.isAll()) {
//      amountWithdraw = passbook.getAmount();
//    } else {
//      amountWithdraw = request.getAmount();
//
//    }
//
//
//    passbook.setAmount(amount - amountWithdraw);
//    customer.setBalance(balance + amountWithdraw);
//    passbookRepository.save(passbook);
//    customerRepository.save(customer);
//
//    String code = generateRandomString();
//
//    Receipt receipt = new Receipt();
//    receipt.setAmount(amount);
//    receipt.setPassbook(passbook);
//    receipt.setCode(code);
//    receipt.setCreatedAt(request.getCreatedAt());
//    receiptRepository.save(receipt);
    ShortTokenReceipt shortTokenReceipt = new ShortTokenReceipt(request.getAmount(),passbook.getId(), otpNew.getId(), request.isAll());

    return new WithdrawalResponse(jwtUtilities.generateTokenShort(customer.getEmail(),shortTokenReceipt));


  }


}
