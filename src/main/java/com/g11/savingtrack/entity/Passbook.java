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
  @JoinColumn(name = "customer_id")
  private Customer customer;
  @ManyToOne
  @JoinColumn(name = "saving_product_id")
  private SavingProduct savingProduct;
  private long amount;
  private String paymentMethod;
  private Date createdAt;


}
