package com.g11.savingtrack.dto.response;

import com.g11.savingtrack.entity.Account;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    private int id;
    private String identityCardNumber;
    private Account account;
    private String accountNumber;
    private String pin;
    private Long balance;
    private String email;
    private String phoneNumber;
    private Date dob;
    private String fullName;
    private String address;
    private String gender;
    private String career;
    private long income;
}
