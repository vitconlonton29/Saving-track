package com.g11.savingtrack.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class PassbookResponse {
  private int id;
  private int employeeId;
  private int customerId;
  private int month;
  private double interestRate;
  private long amount;
  private String typeSaving;
  private Date createdAt;
}
