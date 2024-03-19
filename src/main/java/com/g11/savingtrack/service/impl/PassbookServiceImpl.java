package com.g11.savingtrack.service.impl;

import com.g11.savingtrack.dto.request.PassbookRequest;
import com.g11.savingtrack.dto.response.PassbookResponse;
import com.g11.savingtrack.entity.Passbook;
import com.g11.savingtrack.repository.PassbookRepository;
import com.g11.savingtrack.service.PassbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.g11.savingtrack.utils.MapperUtils.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PassbookServiceImpl implements PassbookService {
  private final PassbookRepository repository;

  public PassbookResponse create(PassbookRequest request) {
    log.info("request:{}", request);

    Passbook passbook = Passbook.from(request);

    return toDTO(repository.save(passbook), PassbookResponse.class);

  }
}
