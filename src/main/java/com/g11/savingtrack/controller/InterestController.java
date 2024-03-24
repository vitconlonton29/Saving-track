package com.g11.savingtrack.controller;

import com.g11.savingtrack.dto.ResponseGeneral;
import com.g11.savingtrack.dto.request.InterestRequest;
import com.g11.savingtrack.dto.response.InterestResponse;
import com.g11.savingtrack.service.InterestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.g11.savingtrack.constants.PassbookManagerConstants.MessageCode.*;

@RestController
@RequestMapping("api/saving/interest")
@RequiredArgsConstructor
@Slf4j
public class InterestController {
  private final InterestService interestService;

  @PostMapping
  public ResponseGeneral<InterestResponse> calculator(@RequestBody InterestRequest request) {
    log.info("(calculator) request:{}", request);

    return ResponseGeneral.ofCreated(
          SUCCESS,
          interestService.calculator(request)
    );
  }
}
