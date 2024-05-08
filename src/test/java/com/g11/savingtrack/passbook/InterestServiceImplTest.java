package com.g11.savingtrack.passbook;

import com.g11.savingtrack.dto.request.InterestRequest;
import com.g11.savingtrack.dto.response.InterestResponse;
import com.g11.savingtrack.entity.Passbook;
import com.g11.savingtrack.entity.SavingProduct;
import com.g11.savingtrack.repository.SavingProductRepository;
import com.g11.savingtrack.service.impl.InterestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class InterestServiceImplTest {

  InterestServiceImpl interestService;

  @Mock
  SavingProductRepository savingProductRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    interestService = new InterestServiceImpl(savingProductRepository);
  }

  @Test
  void calculatorForInterestRequest() {
    SavingProduct savingProduct2 = new SavingProduct();
    savingProduct2.setInterestRate(4.6); // Ví dụ: giá trị interestRate là 0.05

// Thiết lập hành vi cho phương thức findByTerm(int) của savingProductRepository
    when(savingProductRepository.findByTerm(anyInt())).thenReturn(savingProduct2);
    // Tạo yêu cầu tính lãi
    InterestRequest request = new InterestRequest();
    request.setTerm(3);
    request.setInterestRate(4.6);
    request.setAmount(340000000);
    request.setStartDate(new Date(123, Calendar.JANUARY, 5));
    request.setEndDate(new Date(124, Calendar.JANUARY, 10));
    request.setPaymentMethod(1);

    // Tạo một đối tượng SavingProduct
    SavingProduct savingProduct = new SavingProduct();
    savingProduct.setInterestRate(4.6); // Thiết lập giá trị lãi suất
    // Không cần thiết lập giá trị cho các thuộc tính khác vì không sử dụng trong bài kiểm tra này

    // Cài đặt giá trị trả về cho phương thức findByTerm() của repository
    //when(savingProductRepository.findByTerm(3)).thenReturn(savingProduct);

    // Gọi phương thức tính toán lãi
    InterestResponse response = interestService.calculator(request);

    // Kiểm tra kết quả
    assertEquals(16136133, response.getInterest());

    assertEquals(356136133, response.getTotal());
  }


  @Test
  void calculatorForPassbook() {
    // Tạo đối tượng passbook
    Passbook passbook = new Passbook();
    passbook.setAmount(340000000);
    passbook.setCreatedAt(new Date(124, Calendar.JANUARY, 5));

    // Tạo đối tượng savingProduct và thiết lập thuộc tính
    SavingProduct savingProduct = new SavingProduct();
    savingProduct.setTerm(12);
    savingProduct.setInterestRate(6.4);
    passbook.setSavingProduct(savingProduct);

    // Gọi phương thức tính lãi
    Long interest = interestService.calculator(passbook);

    // Kiểm tra kết quả
    assertEquals(21819617, interest);
  }
}
