package com.g11.savingtrack.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String identityCardNumber;
  private String fullName;
  private String gender;
  private String email;
  private String address;
  private String career;
  private double income;
  private String taxCode;


}
