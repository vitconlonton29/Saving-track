package com.g11.savingtrack.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class WithdrawalResponse {
  private int customerId;
  private int passbookId;
  private long amount;
  private String code;
  private Date createdAt;
}
