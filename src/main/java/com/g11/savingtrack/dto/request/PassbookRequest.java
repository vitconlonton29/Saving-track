package com.g11.savingtrack.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassbookRequest {
  private int customerId;
  private int savingProductId;
  private long amount;
  private int paymentMethod;
  private Date createdAt;
}
