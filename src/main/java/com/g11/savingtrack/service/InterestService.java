package com.g11.savingtrack.service;

import com.g11.savingtrack.dto.request.InterestRequest;
import com.g11.savingtrack.dto.response.InterestResponse;
import org.springframework.stereotype.Service;

@Service
public interface InterestService {
  InterestResponse calculator(InterestRequest request);
}
