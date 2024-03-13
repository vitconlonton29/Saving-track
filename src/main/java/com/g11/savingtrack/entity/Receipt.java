package com.g11.savingtrack.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Receipt {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @ManyToOne
  @JoinColumn(name = "employee_id")
  private Employee employee;
  @ManyToOne
  @JoinColumn(name = "passbook_id")
  private Passbook passbook;
  private Date createdAt;
  private long amount;
  private String type;
}
