package com.g11.savingtrack.exception.savingproduct;

import com.g11.savingtrack.exception.base.NotFoundException;

public class SavingProductNotFoundException extends NotFoundException {
  public SavingProductNotFoundException() {
    setMessage("Saving Product not found");
  }
}
