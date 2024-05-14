package com.g11.savingtrack.service.impl;

import com.g11.savingtrack.dto.request.PassbookRequest;
import com.g11.savingtrack.dto.request.VerifyWithdrawalRequest;
import com.g11.savingtrack.dto.response.PassbookResponse;
import com.g11.savingtrack.dto.response.VerifyWithdrawalResponse;
import com.g11.savingtrack.entity.*;
import com.g11.savingtrack.exception.Otp.OtpNotFoundException;
import com.g11.savingtrack.exception.account.AccountNotFoundException;
import com.g11.savingtrack.exception.account.IncomeNotEnoughtMoney;
import com.g11.savingtrack.exception.customer.CustomerNotFoundException;
import com.g11.savingtrack.exception.passbook.PassbookNotFoundException;
import com.g11.savingtrack.exception.savingproduct.SavingProductNotFoundException;
import com.g11.savingtrack.repository.*;
import com.g11.savingtrack.security.JwtUtilities;
import com.g11.savingtrack.service.InterestService;
import com.g11.savingtrack.service.PassbookService;
import com.g11.savingtrack.utils.ShortTokenReceipt;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class PassbookServiceImpl implements PassbookService {
  private final PassbookRepository passbookRepository;
  private final CustomerRepository customerRepository;
  private final SavingProductRepository savingProductRepository;
  private final JwtUtilities jwtUtilities;
  private final OtpRepository otpRepository;
  private final AccountRepository accountRepository;
  private final ReceiptRepository receiptRepository;
  private final InterestService interestService;

  @Override
  public PassbookResponse create(PassbookRequest request) {
    log.info("(create) request:{}", request);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();

    Account account = accountRepository.findByUsername(username).orElseThrow(AccountNotFoundException::new);
    int accountId = account.getId();

    Customer customer = customerRepository.findById(accountId).orElseThrow(CustomerNotFoundException::new);

    Passbook passbook = Passbook.from(request);
    passbook.setCustomer(customer);

    Optional<SavingProduct> savingProduct = savingProductRepository.findById(request.getSavingProductId());
    if (savingProduct.isEmpty()) throw new SavingProductNotFoundException();

    passbook.setSavingProduct(savingProduct.get());
    if(customer.getIncome()<request.getAmount()){
        throw  new IncomeNotEnoughtMoney();
    }
    customer.setIncome(customer.getIncome()-request.getAmount());
    customerRepository.save(customer);
    return PassbookResponse.from(passbookRepository.save(passbook));
  }

  @Override
  public List<PassbookResponse> list() {
    log.info("(list)");
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();

    Account account = accountRepository.findByUsername(username).orElseThrow(AccountNotFoundException::new);

    List<Passbook> passbooks = passbookRepository.findAllByCustomerId(account.getId());
    List<PassbookResponse> passbookResponses = new ArrayList<>();
    for (Passbook p : passbooks) {
      passbookResponses.add(PassbookResponse.from(p));
    }

    return passbookResponses;
  }

  @Override
  public PassbookResponse detail(int passbookId) {
    log.info("detail");

    Passbook passbook = passbookRepository.findById(passbookId).orElseThrow(PassbookNotFoundException::new);

    Long interest = interestService.calculator(passbook);

    PassbookResponse passbookResponse = PassbookResponse.from(passbook);
    passbookResponse.setInterest(interest);

    return passbookResponse;
  }


  @Override
  public VerifyWithdrawalResponse withdraw(HttpServletRequest request, VerifyWithdrawalRequest verifyWithdrawalRequest, int passbookId) {

    String tokenShort = jwtUtilities.getTokenShort(request);
    LinkedHashMap shortTokenReceiptHashMap = jwtUtilities.getClaimValueFromToken(tokenShort, "values", LinkedHashMap.class);
    ShortTokenReceipt shortTokenReceipt = ShortTokenReceipt.fromLinkedHashMap(shortTokenReceiptHashMap);

    Otp otp = otpRepository.findById(shortTokenReceipt.getIdOtp()).orElseThrow(() -> new OtpNotFoundException());
    LocalDateTime dateTime1 = LocalDateTime.now();
    LocalDateTime dateTime2 = otp.getDateTimeCreate();
    Duration duration = Duration.between(dateTime1, dateTime2);
    long seconds = Math.abs(duration.getSeconds());
    if (seconds > 60 || !String.valueOf(verifyWithdrawalRequest.getCode()).equals(otp.getCode())) {
      throw new OtpNotFoundException();
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    Account account = accountRepository.findByUsername(username).orElseThrow(AccountNotFoundException::new);
    Passbook passbook = passbookRepository.findById(shortTokenReceipt.getIdPassbook()).orElseThrow(PassbookNotFoundException::new);

    Customer customer = customerRepository.findById(account.getId()).orElseThrow(CustomerNotFoundException::new);


    long amountWithdraw;
    long amount = passbook.getAmount();
    long balance = customer.getBalance();

    if (shortTokenReceipt.isAll()) {
      amountWithdraw = passbook.getAmount();
    } else {
      amountWithdraw = shortTokenReceipt.getAmount();

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
    receipt.setCreatedAt(new Date());
    Receipt receiptNew = receiptRepository.save(receipt);


    return new VerifyWithdrawalResponse(customer.getId(), passbook.getId(), amount, code, receiptNew.getCreatedAt());
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
