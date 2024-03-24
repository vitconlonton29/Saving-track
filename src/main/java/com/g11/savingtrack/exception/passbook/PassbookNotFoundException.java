package com.g11.savingtrack.exception.passbook;

import com.g11.savingtrack.exception.base.NotFoundException;

public class PassbookNotFoundException extends NotFoundException {
  public PassbookNotFoundException (){
    setMessage("Passbook not found");
  }
}
