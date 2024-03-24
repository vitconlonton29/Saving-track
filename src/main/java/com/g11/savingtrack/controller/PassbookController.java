package com.g11.savingtrack.controller;

import com.g11.savingtrack.dto.ResponseGeneral;
import com.g11.savingtrack.dto.request.PassbookRequest;
import com.g11.savingtrack.dto.response.PassbookResponse;
import com.g11.savingtrack.service.PassbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.g11.savingtrack.constants.PassbookManagerConstants.MessageCode.*;

@RestController
@RequestMapping("api/saving/customers/{id}/passbooks")
@RequiredArgsConstructor
@Slf4j
public class PassbookController {
  private final PassbookService passbookService;

  @PostMapping
  public ResponseGeneral<PassbookResponse> create(@PathVariable int id, @RequestBody PassbookRequest request) {
    log.info("(create) customer_id:{} request:{}", id, request);

    request.setCustomerId(id);
    return ResponseGeneral.ofCreated(
          SUCCESS,
          passbookService.create(request)
    );
  }

}
