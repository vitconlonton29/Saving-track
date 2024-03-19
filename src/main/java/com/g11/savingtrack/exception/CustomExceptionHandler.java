package com.g11.savingtrack.exception;
import com.g11.savingtrack.exception.customer.CustomerNotFoundException;
import com.g11.savingtrack.exception.passbook.PassbookAlreadyWithdaw;
import com.g11.savingtrack.exception.passbook.PassbookNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(PassbookNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handlePassbookNotFoundExceptionn(PassbookNotFoundException ex, WebRequest request) {
        CustomErrorResponse error = new CustomErrorResponse();
        error.setCode(ex.getCode());
        error.setStatus(ex.getStatus());
        error.setParams(ex.getParams());
        error.setMessage(ex.getMessage());
        error.setPath(request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PassbookAlreadyWithdaw.class)
    public ResponseEntity<CustomErrorResponse> handlePassbookAlreadyWithdawException(PassbookAlreadyWithdaw ex, WebRequest request) {
        CustomErrorResponse error = new CustomErrorResponse();
        error.setCode(ex.getCode());
        error.setStatus(ex.getStatus());
        error.setParams(ex.getParams());
        error.setMessage(ex.getMessage());
        error.setPath(request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
