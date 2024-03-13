package com.g11.savingtrack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Expire {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private long interest;
  private long total;
  private Date start;
}
