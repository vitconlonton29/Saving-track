package com.g11.savingtrack.exception.base;


import static com.g11.savingtrack.constants.PassbookManagerConstants.StatusException.*;

public class BadRequestException extends BaseException {
  public BadRequestException() {
    setCode("com.ncsgroup.profiling.exception.base.BadRequestException");
    setStatus(BAD_REQUEST);
  }
}
