package com.g11.savingtrack.passbook;


import com.g11.savingtrack.dto.request.PassbookRequest;
import com.g11.savingtrack.dto.request.VerifyWithdrawalRequest;
import com.g11.savingtrack.dto.response.PassbookResponse;
import com.g11.savingtrack.dto.response.VerifyWithdrawalResponse;
import com.g11.savingtrack.entity.*;
import com.g11.savingtrack.repository.*;
import com.g11.savingtrack.security.JwtUtilities;
import com.g11.savingtrack.service.InterestService;
import com.g11.savingtrack.service.impl.PassbookServiceImpl;
import com.g11.savingtrack.utils.DateUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PassbookServiceImplTest {

  @InjectMocks
  PassbookServiceImpl passbookService;

  @Mock
  PassbookRepository passbookRepository;

  @Mock
  CustomerRepository customerRepository;

  @Mock
  SavingProductRepository savingProductRepository;

  @Mock
  JwtUtilities jwtUtilities;

  @Mock
  OtpRepository otpRepository;

  @Mock
  AccountRepository accountRepository;

  @Mock
  ReceiptRepository receiptRepository;

  @Mock
  InterestService interestService;

  @Mock
  HttpServletRequest request;

  @Mock
  SecurityContext securityContext;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  public void createPassbook_ValidDTO(){
    PassbookRequest passbookRequest = new PassbookRequest();
    passbookRequest.setSavingProductId(2);
    passbookRequest.setAmount(10000000);
    passbookRequest.setCreatedAt(new Date());
    passbookRequest.setPaymentMethod(1);

    Authentication authentication = mock(Authentication.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("Hau2903");

    Account account = new Account();
    account.setId(2);
    when(accountRepository.findByUsername("Hau2903")).thenReturn(java.util.Optional.of(account));

    Customer customer = new Customer();
    customer.setId(2);
    List<Customer> customers = new ArrayList<>();
    customers.add(customer);
    when(customerRepository.findByAccountId(2)).thenReturn(customers);

    SavingProduct savingProduct = new SavingProduct();
    savingProduct.setId(1);
    when(savingProductRepository.findById(1)).thenReturn(java.util.Optional.of(savingProduct));

    Passbook passbook = new Passbook();
    passbook.setCustomer(customer);
    passbook.setId(2);
    passbook.setSavingProduct(savingProduct);
    when(passbookRepository.save(any(Passbook.class))).thenReturn(passbook);

    // Gọi phương thức cần kiểm tra
    PassbookResponse passbookResponse = passbookService.create(passbookRequest);

    // Kiểm tra kết quả
    assertEquals(2, passbookResponse.getId());
    assertEquals(2, passbookResponse.getId());
  }

  @Test
  void createPassbook() {

  }

  @Test
  void listPassbooks() {
    // Chuẩn bị dữ liệu cho bài kiểm tra
    Authentication authentication = mock(Authentication.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("test_user");

    Account account = new Account();
    account.setId(1);
    when(accountRepository.findByUsername("test_user")).thenReturn(java.util.Optional.of(account));

    Customer customer = new Customer();
    customer.setId(1);
    List<Customer> customers = new ArrayList<>();
    customers.add(customer);
    when(customerRepository.findByAccountId(1)).thenReturn(customers);

    SavingProduct savingProduct = new SavingProduct();
    savingProduct.setId(1);
    when(savingProductRepository.findById(1)).thenReturn(java.util.Optional.of(savingProduct));

    Passbook passbook = new Passbook();
    passbook.setCustomer(customer);
    passbook.setSavingProduct(savingProduct);
    passbook.setId(1);
    List<Passbook> passbooks = new ArrayList<>();
    passbooks.add(passbook);
    when(passbookRepository.findAllByCustomerId(customer.getId())).thenReturn(passbooks);

    // Gọi phương thức cần kiểm tra
    List<PassbookResponse> passbookResponses = passbookService.list();

    // Kiểm tra kết quả
    assertEquals(1, passbookResponses.size());
    assertEquals(1, passbookResponses.get(0).getId());
  }

  @Test
  void detailPassbook() {
    // Chuẩn bị dữ liệu cho bài kiểm tra
    SavingProduct savingProduct = new SavingProduct();
    savingProduct.setId(1);
    when(savingProductRepository.findById(1)).thenReturn(java.util.Optional.of(savingProduct));

    Customer customer = new Customer();
    customer.setId(1);
    List<Customer> customers = new ArrayList<>();
    customers.add(customer);
    when(customerRepository.findByAccountId(1)).thenReturn(customers);

    int passbookId = 1;
    Passbook passbook = new Passbook();
    passbook.setId(passbookId);
    passbook.setCustomer(customer);
    passbook.setSavingProduct(savingProduct);
    when(passbookRepository.findById(passbookId)).thenReturn(java.util.Optional.of(passbook));

    // Gọi phương thức cần kiểm tra
    PassbookResponse passbookResponse = passbookService.detail(passbookId);

    // Kiểm tra kết quả
    assertEquals(passbookId, passbookResponse.getId());
  }

  @Test
  void withdraw() {
    // Chuẩn bị dữ liệu cho bài kiểm tra
    HttpServletRequest request = mock(HttpServletRequest.class);
    VerifyWithdrawalRequest verifyWithdrawalRequest = new VerifyWithdrawalRequest();
    verifyWithdrawalRequest.setCode(123456);
    int passbookId = 1;

    // Mocking token related methods
    when(jwtUtilities.getTokenShort(request)).thenReturn("ShortToken");
    LinkedHashMap<String, Object> shortTokenReceiptHashMap = mock(LinkedHashMap.class);
    when(jwtUtilities.getClaimValueFromToken(eq("ShortToken"), eq("values"), eq(LinkedHashMap.class))).thenReturn(shortTokenReceiptHashMap);

    // Thiết lập giá trị cho shortTokenReceiptHashMap
    when(shortTokenReceiptHashMap.get("amount")).thenReturn(100);
    when(shortTokenReceiptHashMap.get("idPassbook")).thenReturn(passbookId);
    when(shortTokenReceiptHashMap.get("idOtp")).thenReturn(1);
    when(shortTokenReceiptHashMap.get("isAll")).thenReturn(true);

    Authentication authentication = mock(Authentication.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("test_user");

    Otp otp = new Otp();
    otp.setId(1);
    otp.setCode("123456");
    otp.setDateTimeCreate(LocalDateTime.now());
    when(otpRepository.findById(1)).thenReturn(java.util.Optional.of(otp));

    Account account = new Account();
    account.setId(1);
    account.setUsername("test_user");
    when(accountRepository.findByUsername("test_user")).thenReturn(java.util.Optional.of(account));

    Passbook passbook = new Passbook();
    passbook.setId(passbookId);
    when(passbookRepository.findById(1)).thenReturn(java.util.Optional.of(passbook));

    Customer customer = new Customer();
    customer.setId(1);
    customer.setBalance(10000000L);
    List<Customer> customers = new ArrayList<>();
    customers.add(customer);
    when(customerRepository.findByAccountId(1)).thenReturn(customers);

    when(shortTokenReceiptHashMap.get("idPassbook")).thenReturn(passbookId);

    Receipt receipt = new Receipt();
    receipt.setPassbook(passbook);
    receipt.setCreatedAt(new Date());
    when(receiptRepository.save(any(Receipt.class))).thenReturn(receipt);
    // Gọi phương thức cần kiểm tra
    VerifyWithdrawalResponse verifyWithdrawalResponse = passbookService.withdraw(request, verifyWithdrawalRequest, passbookId);

    // Kiểm tra kết quả
    assertEquals(1, verifyWithdrawalResponse.getCustomerId());
    assertEquals(passbookId, verifyWithdrawalResponse.getPassbookId());
    //assertEquals("123456", verifyWithdrawalResponse.getCode());
  }
}
