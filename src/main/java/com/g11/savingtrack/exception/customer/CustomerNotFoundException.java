package com.g11.savingtrack.exception.customer;

import com.g11.savingtrack.exception.base.NotFoundException;

public class CustomerNotFoundException extends NotFoundException {
  public CustomerNotFoundException(){
    setMessage("Customer not found");
  }
}
