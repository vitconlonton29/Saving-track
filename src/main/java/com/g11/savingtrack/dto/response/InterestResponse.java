package com.g11.savingtrack.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class InterestResponse {
  private int passbookId;
  private String customerName;
  private long amount;
  private int month;
  private double interestRate;
  private Date startDate;
  private Date endDate;
  private long interest;

}
