package com.g11.savingtrack.dto.request;

import com.g11.savingtrack.entity.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String identityCardNumber;
    private String accountNumber;
    private String pin;
    private String phoneNumber;
}
