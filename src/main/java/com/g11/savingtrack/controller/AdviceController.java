package com.g11.savingtrack.controller;

import com.g11.savingtrack.dto.Error;
import com.g11.savingtrack.dto.ResponseGeneral;
import com.g11.savingtrack.exception.account.AccountNotFoundException;
import com.g11.savingtrack.exception.account.IncomeNotEnoughtMoney;
import com.g11.savingtrack.exception.base.BaseException;
import com.g11.savingtrack.exception.base.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class AdviceController {
  private final MessageSource messageSource;

  @ExceptionHandler(IncomeNotEnoughtMoney.class)
  public ResponseEntity<ResponseGeneral<String>> handleNotFoundException(IncomeNotEnoughtMoney ex) {
    return new ResponseEntity<>(ResponseGeneral.of(400, "Bad Request", ex.getMes()), HttpStatus.BAD_REQUEST);

  }
  @ExceptionHandler(AccountNotFoundException.class)
  public ResponseEntity<ResponseGeneral<String>> handleNotFoundException(AccountNotFoundException ex) {
    return new ResponseEntity<>(ResponseGeneral.of(404, "Not Found", ex.getMess()), HttpStatus.BAD_REQUEST);

  }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseGeneral<Error>> handleNotFoundException(NotFoundException ex, WebRequest webRequest) {
    return ResponseEntity
            .status(ex.getStatus())
            .body(getError(ex.getStatus(), ex.getCode(), webRequest.getLocale(), ex.getParams()));
  }

  @ExceptionHandler (value = {BaseException.class})
  public ResponseEntity<ResponseGeneral<Error>> handleFinanceBaseException(
        BaseException ex,
        WebRequest webRequest
  ) {
    return ResponseEntity
          .status(ex.getStatus())
          .body(getError(ex.getStatus(), ex.getCode(), webRequest.getLocale(), ex.getParams()));
  }


  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseGeneral<Error>> handleValidationExceptions(
        MethodArgumentNotValidException exception,
        WebRequest webRequest
  ) {

    String errorMessage = exception.getBindingResult().getFieldErrors().stream()
          .map(fieldError -> fieldError.getDefaultMessage())
          .findFirst()
          .orElse(exception.getMessage());

    log.error("(handleValidationExceptions) {}", errorMessage);
    return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(getError(HttpStatus.BAD_REQUEST.value(), errorMessage, "English"));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ResponseGeneral<Error>> handleConstraintViolationExceptions(
        ConstraintViolationException exception,
        WebRequest webRequest
  ) {


    String errorMessage = exception.getConstraintViolations().stream()
          .map(constraintViolation -> constraintViolation.getMessage())
          .findFirst()
          .orElse(exception.getMessage());

    log.error("(handleConstraintViolationExceptions) {}", errorMessage);
    return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(getError(HttpStatus.BAD_REQUEST.value(), errorMessage, "English"));
  }

  private ResponseGeneral<Error> getError(int status, String code, String language) {
    return ResponseGeneral.of(
          status,
          HttpStatus.valueOf(status).getReasonPhrase(),
          Error.of(code, getMessage(code, new Locale(language)))
    );
  }

  private ResponseGeneral<Error> getError(int status, String code, Map<String, String> params) {
    return ResponseGeneral.of(
          status,
          HttpStatus.valueOf(status).getReasonPhrase(),
          Error.of(code, params)
    );
  }

  private ResponseGeneral<Error> getError(int status, String code, Locale locale, Map<String, String> params) {
    return ResponseGeneral.of(
          status,
          HttpStatus.valueOf(status).getReasonPhrase(),
          Error.of(code, getMessage(code, locale, params))
    );
  }

  private String getMessage(String code, Locale locale, Map<String, String> params) {
    var message = getMessage(code, locale);
    if (params != null && !params.isEmpty()) {
      for (var param : params.entrySet()) {
        message = message.replace(getMessageParamsKey(param.getKey()), param.getValue());
      }
    }
    return message;
  }

  private String getMessage(String code, Locale locale) {
    try {
      return messageSource.getMessage(code, null, locale);
    } catch (Exception ex) {
      return code;
    }
  }

  private String getMessageParamsKey(String key) {
    return "%" + key + "%";
  }
}
