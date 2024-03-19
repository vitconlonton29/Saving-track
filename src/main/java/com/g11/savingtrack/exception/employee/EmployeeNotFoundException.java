package com.g11.savingtrack.exception.employee;

import com.g11.savingtrack.exception.base.NotFoundException;

public class EmployeeNotFoundException extends NotFoundException {
  public EmployeeNotFoundException(){
    setMessage("Employee not found");
  }
}
