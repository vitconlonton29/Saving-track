package com.g11.savingtrack.service.impl;

import com.g11.savingtrack.dto.request.WithdrawalRequest;
import com.g11.savingtrack.dto.response.WithdrawalResponse;
import com.g11.savingtrack.entity.Customer;
import com.g11.savingtrack.entity.Otp;
import com.g11.savingtrack.entity.Passbook;
import com.g11.savingtrack.entity.Receipt;
import com.g11.savingtrack.exception.customer.CustomerNotFoundException;
import com.g11.savingtrack.exception.passbook.PassbookNotFoundException;
import com.g11.savingtrack.repository.CustomerRepository;
import com.g11.savingtrack.repository.PassbookRepository;
import com.g11.savingtrack.repository.ReceiptRepository;
import com.g11.savingtrack.service.EmailService;
import com.g11.savingtrack.service.OtpService;
import com.g11.savingtrack.service.WithdrawalService;
import com.g11.savingtrack.utils.EmailUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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


  @Override
  public WithdrawalResponse withDraw(WithdrawalRequest request) {
    log.info("(withDraw) request:{}", request);

    Passbook passbook = passbookRepository.findById(request.getPassbookId()).orElseThrow(PassbookNotFoundException::new);
    Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(CustomerNotFoundException::new);
    //gui OTP qua mail de xac thuc
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
    otpService.saveOtp(otp);



    long amountWithdraw;
    long amount = passbook.getAmount();
    long balance = customer.getBalance();

    if (request.isAll()) {
      amountWithdraw = passbook.getAmount();
    } else {
      amountWithdraw = request.getAmount();

    }


    passbook.setAmount(amount - amountWithdraw);
    customer.setBalance(balance + amountWithdraw);
    passbookRepository.save(passbook);
    customerRepository.save(customer);

    String code = generateRandomString();

    Receipt receipt = new Receipt();
    receipt.setAmount(amount);
    receipt.setPassbook(passbook);
    receipt.setCode(code);
    receipt.setCreatedAt(request.getCreatedAt());
    receiptRepository.save(receipt);

    return new WithdrawalResponse(customer.getId(), passbook.getId(), amount, code, request.getCreatedAt());


  }

  private String generateRandomString() {
    String uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    StringBuilder sb = new StringBuilder();
    Random random = new Random();

    for (int i = 0; i < 10; i++) {
      char randomChar = uppercaseLetters.charAt(random.nextInt(uppercaseLetters.length()));
      sb.append(randomChar);
    }

    return sb.toString();
  }
}
