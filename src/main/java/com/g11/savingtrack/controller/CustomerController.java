package com.g11.savingtrack.controller;

import com.g11.savingtrack.dto.ResponseGeneral;
import com.g11.savingtrack.dto.request.CustomerRequest;
import com.g11.savingtrack.dto.response.CustomerResponse;
import com.g11.savingtrack.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.g11.savingtrack.constants.PassbookManagerConstants.MessageCode.*;

@RestController
@RequestMapping("/passbook/api/v1/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {
  private final CustomerService customerService;

  @PostMapping
  public ResponseGeneral<CustomerResponse> create(@RequestBody CustomerRequest request) {
    log.info("(create) request:{}", request);

    return ResponseGeneral.ofCreated(
          SUCCESS,
          customerService.create(request)
    );

  }
}
