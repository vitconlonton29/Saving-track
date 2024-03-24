package com.g11.savingtrack.service;

import com.g11.savingtrack.dto.request.PassbookRequest;
import com.g11.savingtrack.dto.response.PassbookResponse;

public interface PassbookService {
  PassbookResponse create(PassbookRequest request);
}
