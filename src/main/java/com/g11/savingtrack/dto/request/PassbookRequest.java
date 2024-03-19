package com.g11.savingtrack.dto.request;


import lombok.Data;

import java.util.Date;

@Data
public class PassbookRequest {
  private int employeeId;
  private int customerId;
  private int month;
  private long amount;
  private Date createdAt;
}
