package com.g11.savingtrack.dto.request;


import lombok.Data;

import java.util.Date;

@Data
public class PassbookRequest {

  private String employeeName;
  private String identityCardNumber;
  private String fullName;
  private String gender;
  private String email;
  private String address;
  private String career;
  private double income;
  private String taxCode;
  private int month;
  private double interestRate;
  private long amount;
  private String typeSaving;
  private Date createdAt;
}
