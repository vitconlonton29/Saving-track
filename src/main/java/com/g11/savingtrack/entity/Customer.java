package com.g11.savingtrack.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String identityCardNumber;
  @ManyToOne
  @JoinColumn(name = "account_id")
  private Account account;
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
