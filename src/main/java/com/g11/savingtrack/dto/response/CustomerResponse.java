package com.g11.savingtrack.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerResponse {
  private String id;
  private String identityCardNumber;
  private Date dob;
  private String fullName;
  private String gender;
  private String email;
  private String address;
  private String career;
  private double income;
  private String taxCode;
}
