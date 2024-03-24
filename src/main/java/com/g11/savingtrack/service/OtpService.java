package com.g11.savingtrack.service;

import com.g11.savingtrack.entity.Otp;

import java.util.List;
import java.util.Optional;

public interface OtpService {
     Otp saveOtp(Otp otp);
     List<Otp> findLatestOtpByCustomerId(int customerId);
}
