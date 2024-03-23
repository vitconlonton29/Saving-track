package com.g11.savingtrack.dto.response;

import com.g11.savingtrack.entity.Passbook;
import lombok.Data;

import java.util.Date;

@Data
public class PassbookResponse {
  private int id;
  private int employeeId;
  private int customerId;
  private int month;
  private double interestRate;
  private long amount;
  private Date createdAt;

  public static PassbookResponse from(Passbook passbook) {
    PassbookResponse pr = new PassbookResponse();
    pr.id = passbook.getId();
    pr.employeeId = passbook.getAccount().getId();
    pr.customerId = passbook.getCustomer().getId();
    pr.month = passbook.getPeriod().getMonth();
    pr.interestRate = passbook.getPeriod().getInterestRate();
    pr.amount = passbook.getAmount();
    pr.createdAt = passbook.getCreatedAt();

    return pr;
  }
}
