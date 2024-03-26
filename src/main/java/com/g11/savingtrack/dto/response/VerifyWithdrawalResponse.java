package com.g11.savingtrack.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@AllArgsConstructor
@Data
@NoArgsConstructor
public class VerifyWithdrawalResponse {
    private int customerId;
    private int passbookId;
    private long amount;
    private String code;
    private Date createdAt;
}
