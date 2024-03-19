package com.g11.savingtrack.service;

import com.g11.savingtrack.dto.request.PassbookRequest;
import com.g11.savingtrack.dto.response.PassbookResponse;
import org.springframework.stereotype.Service;

@Service
public interface PassbookService {
  PassbookResponse create(PassbookRequest request);
}
