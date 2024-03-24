package com.g11.savingtrack.exception.Otp;

import com.g11.savingtrack.exception.base.NotFoundException;

public class OtpNotFoundException extends NotFoundException {
    public OtpNotFoundException(){
        setMessage("Otp not found");
    }
}
