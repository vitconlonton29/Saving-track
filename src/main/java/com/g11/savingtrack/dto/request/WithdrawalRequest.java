package com.g11.savingtrack.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawalRequest {
  private int customerId;
  private int passbookId;
  private long amount;
  private Date createdAt;
  private boolean isAll;
}
