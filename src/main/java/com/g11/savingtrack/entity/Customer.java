package com.g11.savingtrack.entity;

import com.g11.savingtrack.dto.request.PassbookRequest;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String identityCardNumber;
  private String accountNumber;
  private String pin;
  private Long balance;
  private String email;
  private String phoneNumber;
  private Date dob;
  private String fullName;
  private String address;
  private String gender;
  private String career;
  private long income;
}
