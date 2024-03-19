package com.g11.savingtrack.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Expire {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @ManyToOne
  @JoinColumn(name = "passbook_id")
  private Passbook passbook;
  private long interest;
  private long total;
  private Date start;
}
