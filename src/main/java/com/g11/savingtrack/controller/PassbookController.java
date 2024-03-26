package com.g11.savingtrack.controller;

import com.g11.savingtrack.dto.ResponseGeneral;
import com.g11.savingtrack.dto.request.PassbookRequest;
import com.g11.savingtrack.dto.request.VerifyWithdrawalRequest;
import com.g11.savingtrack.dto.request.WithdrawalRequest;
import com.g11.savingtrack.dto.response.PassbookResponse;
import com.g11.savingtrack.dto.response.VerifyWithdrawalResponse;
import com.g11.savingtrack.dto.response.WithdrawalResponse;
import com.g11.savingtrack.security.JwtUtilities;
import com.g11.savingtrack.service.PassbookService;
import com.g11.savingtrack.service.WithdrawalService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.g11.savingtrack.constants.PassbookManagerConstants.MessageCode.*;

@RestController
@RequestMapping("api/saving/customers/{id}/passbooks")
@RequiredArgsConstructor
@Slf4j
public class PassbookController {
  private final PassbookService passbookService;
  private final WithdrawalService withdrawalService;

  @PostMapping
  public ResponseGeneral<PassbookResponse> create(@PathVariable int id, @RequestBody PassbookRequest request) {
    log.info("(create) customer_id:{} request:{}", id, request);

    request.setCustomerId(id);
    return ResponseGeneral.ofCreated(
          SUCCESS,
          passbookService.create(request)
    );
  }

  @PostMapping("/{passbookId}/withdraw")
  public ResponseGeneral<WithdrawalResponse> withDraw(@PathVariable int id, @PathVariable int passbookId, @RequestBody WithdrawalRequest request) {
    log.info("(create) customer_id:{} request:{}", id, request);

    request.setCustomerId(id);
    request.setPassbookId(passbookId);

    return ResponseGeneral.ofCreated(
          SUCCESS,
          withdrawalService.withDraw(request)
    );
  }

  @GetMapping
  public ResponseGeneral<List<PassbookResponse>> list(@PathVariable int id) {
    log.info("(list)");

    return ResponseGeneral.ofCreated(
          SUCCESS,
          passbookService.list(id)
    );
  }

  @GetMapping("/{passbookId}")
  public ResponseGeneral<PassbookResponse> detail(@PathVariable int passbookId) {
    log.info("detail");

    return ResponseGeneral.ofCreated(
          SUCCESS,
          passbookService.detail(passbookId)
    );
  }
  @PreAuthorize("isAuthenticated()")
  @PostMapping("/{passbookId}/verify")
  public ResponseGeneral<VerifyWithdrawalResponse> verify(@PathVariable int passbookId, HttpServletRequest request, @RequestBody VerifyWithdrawalRequest verifyWithdrawalRequest) {
    return ResponseGeneral.ofCreated(SUCCESS,passbookService.withdraw(request,verifyWithdrawalRequest,passbookId));
  }

}
