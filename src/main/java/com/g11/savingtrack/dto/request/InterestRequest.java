package com.g11.savingtrack.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class InterestRequest {
  private long amount;
  private double interestRate;
  private int term;
  private int paymentMethod;
  private Date startDate;
  private Date endDate;
}
