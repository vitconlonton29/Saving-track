package com.g11.savingtrack.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerResponse {
  private String id;
  private String identityCardNumber;
  private String fullName;
  private String gender;
  private String email;
  private String phoneNumber;
  private String address;
  private String career;
  private long income;
  private String taxCode;
  private Date dob;

}
