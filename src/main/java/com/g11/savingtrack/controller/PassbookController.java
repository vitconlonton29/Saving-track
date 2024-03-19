package com.g11.savingtrack.controller;

import com.g11.savingtrack.dto.ResponseGeneral;
import com.g11.savingtrack.dto.request.PassbookRequest;
import com.g11.savingtrack.dto.response.PassbookResponse;
import com.g11.savingtrack.service.PassbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.g11.savingtrack.constants.PassbookManagerConstants.MessageCode.*;

@RestController
@RequestMapping("passbook/api/v1/passbooks")
@RequiredArgsConstructor
@Slf4j
public class PassbookController {
  private final PassbookService passbookService;

  @PostMapping
  public ResponseGeneral<PassbookResponse> create(@RequestBody PassbookRequest request) {
    log.info("(create) request:{}", request);

    return ResponseGeneral.ofCreated(
          SUCCESS,
          passbookService.create(request)
    );
  }
}
