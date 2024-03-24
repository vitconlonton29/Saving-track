package com.g11.savingtrack.controller;

import com.g11.savingtrack.dto.ResponseGeneral;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  @GetMapping("/BackSaveById/{id}")
  public ResponseEntity<?> getPassbookByid(@PathVariable int id) {
    return new ResponseEntity<>(passbookService.getPassbookById(id), HttpStatus.OK);
  }

  @GetMapping("/BackSave/{cccd}")
  public ResponseEntity<?> getPassbooksByCccd(@PathVariable String cccd) {
    return new ResponseEntity<>(passbookService.getPassbooksByCccd(cccd), HttpStatus.OK);
  }

  @PatchMapping("/BackSaveById/{id}")
  public ResponseEntity<?> withdrawPassbook(@PathVariable int id) {
    return new ResponseEntity<>(passbookService.withdrawPassbook(id), HttpStatus.OK);
  }

  @GetMapping("/{id}/interest")
  public ResponseGeneral<InterestResponse> interest(@PathVariable int id) {
    log.info("(interest) id:{}", id);

    return ResponseGeneral.ofCreated(
          SUCCESS,
          passbookService.getInterest(id)
    );
  }
}
