package com.g11.savingtrack.entity;

import com.g11.savingtrack.dto.request.PassbookRequest;
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
  @JoinColumn(name = "period_id")
  private Period period;
  private long amount;
  private Date createdAt;
  private Date withdrawAt;
  private String status;

  public static Passbook from(PassbookRequest request) {
    Passbook p = new Passbook();
    p.amount = request.getAmount();
    p.createdAt = request.getCreatedAt();
    return p;
  }
}
