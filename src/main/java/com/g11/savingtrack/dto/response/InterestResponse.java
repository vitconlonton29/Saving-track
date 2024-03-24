package com.g11.savingtrack.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InterestResponse {
  private long amount;
  private long interest;
  private long total;
}
