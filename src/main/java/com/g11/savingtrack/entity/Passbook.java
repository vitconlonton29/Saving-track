package com.g11.savingtrack.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Passbook {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @ManyToOne
  @JoinColumn(name = "employee_id")
  private Employee employee;
  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Customer customer;
  @ManyToOne
  @JoinColumn(name = "expire_id")
  private Expire expire;
  @ManyToOne
  @JoinColumn(name= "period_id")
  private Period period;
  private double amount;
  private String typeSaving;
  private Date createdAt;
}
