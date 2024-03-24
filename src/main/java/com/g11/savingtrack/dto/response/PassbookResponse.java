package com.g11.savingtrack.dto.response;

import com.g11.savingtrack.entity.Passbook;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassbookResponse {
  private int id;
  private int customerId;
  private int term;
  private double interestRate;
  private long amount;
  private String paymentMethod;
  private Date createdAt;

  public static PassbookResponse from(Passbook passbook) {
    PassbookResponse pr = new PassbookResponse();
    pr.id = passbook.getId();
    pr.customerId = passbook.getCustomer().getId();
    pr.term = passbook.getSavingProduct().getTerm();
    pr.interestRate = passbook.getSavingProduct().getInterestRate();
    pr.amount = passbook.getAmount();
    pr.createdAt = passbook.getCreatedAt();

    return pr;
  }
}
