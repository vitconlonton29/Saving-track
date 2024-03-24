package com.g11.savingtrack.service.impl;
import com.g11.savingtrack.entity.Otp;
import com.g11.savingtrack.repository.OtpRepository;
import com.g11.savingtrack.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OtpServiceImpl implements OtpService {
    private final OtpRepository otpRepository;

    @Autowired
    public OtpServiceImpl(OtpRepository otpRepository) {
        this.otpRepository = otpRepository;
    }
    @Override
    public Otp saveOtp(Otp otp) {
        // Lưu đối tượng Otp bằng cách gọi phương thức save từ repository
        return otpRepository.save(otp);
    }
    @Override
    public List<Otp> findLatestOtpByCustomerId(int customerId){
        return otpRepository.findLatestOtpByCustomerId(customerId);
    }
}
