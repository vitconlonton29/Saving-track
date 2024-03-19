package com.g11.savingtrack.exception.period;

import com.g11.savingtrack.exception.base.NotFoundException;

public class PeriodNotFoundException extends NotFoundException {
  public PeriodNotFoundException(){
    setCode("Period not found");
  }
}
