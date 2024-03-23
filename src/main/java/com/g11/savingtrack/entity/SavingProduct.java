package com.g11.savingtrack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class SavingProduct {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private int term;
  private double interestRate;
}
