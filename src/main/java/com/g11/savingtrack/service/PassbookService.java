package com.g11.savingtrack.service;

import com.g11.savingtrack.dto.request.PassbookRequest;
import com.g11.savingtrack.dto.response.PassbookResponse;

import java.util.List;

public interface PassbookService {
  PassbookResponse create(PassbookRequest request);

  List<PassbookResponse> list(int id);
  PassbookResponse detail(int id);
}
