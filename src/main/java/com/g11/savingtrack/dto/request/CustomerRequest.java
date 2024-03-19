package com.g11.savingtrack.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerRequest {
  private String identityCardNumber;
  private String fullName;
  private String gender;
  private String email;
  private String address;
  private String phoneNumber;
  private Date dob;
  private String career;
  private long income;
  private String taxCode;
}
