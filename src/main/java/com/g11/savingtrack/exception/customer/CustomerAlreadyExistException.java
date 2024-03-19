package com.g11.savingtrack.exception.customer;

import com.g11.savingtrack.exception.base.ConflictException;

public class CustomerAlreadyExistException extends ConflictException {
  public CustomerAlreadyExistException() {
    setMessage("Customer already exist");
  }
}
