package com.g11.savingtrack;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import com.g11.savingtrack.entity.Customer;
import com.g11.savingtrack.entity.Otp;
import com.g11.savingtrack.repository.OtpRepository;
import com.g11.savingtrack.service.OtpService;
import com.g11.savingtrack.service.impl.OtpServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;



public class OtpServiceTest {

    @Mock
    private OtpRepository otpRepository;
    @Mock
    private Customer customer;

    @InjectMocks
    private OtpServiceImpl otpService;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    // Test case for saveOtp method
    @Test
    public void testSaveOtpCorrect() {
        Customer customer = new Customer();
        customer.setId(1);
        // Arrange
        Otp otp = new Otp();
        otp.setCustomer(customer);
        otp.setCode("123456");

        // Mock behavior of otpRepository.save method
        when(otpRepository.save(any(Otp.class))).thenReturn(otp);

        // Act
        Otp savedOtp = otpService.saveOtp(otp);

        // Assert
        assertNotNull(savedOtp);
        assertEquals(1, savedOtp.getCustomer().getId());
        assertEquals("123456", savedOtp.getCode());
    }

    // Test case for findLatestOtpByCustomerId method
    @Test
    public void testFindLatestOtpByCustomerId() {
        Customer customer = new Customer();
        customer.setId(1);
        // Arrange
        Otp otp1 = new Otp();
        otp1.setCustomer(customer);
        otp1.setCode("111222");

        Otp otp2 = new Otp();
        otp2.setCustomer(customer);
        otp2.setCode("333444");

        List<Otp> otpList = Arrays.asList(otp1, otp2);

        // Mock behavior of otpRepository.findLatestOtpByCustomerId method
        when(otpRepository.findLatestOtpByCustomerId(customer.getId())).thenReturn(otpList);

        // Act
        List<Otp> latestOtpList = otpService.findLatestOtpByCustomerId(customer.getId());

        // Assert
        assertNotNull(latestOtpList);
        assertEquals(2, latestOtpList.size());
        assertEquals("111222", latestOtpList.get(0).getCode());
        assertEquals("333444", latestOtpList.get(1).getCode());
    }

    // Add more test cases as needed...

}
