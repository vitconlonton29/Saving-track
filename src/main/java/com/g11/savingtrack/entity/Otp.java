package com.g11.savingtrack.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Otp {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Customer customer;
  private String code;
  private LocalDateTime dateTimeCreate;
}
