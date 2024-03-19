package com.g11.savingtrack.service;

import com.g11.savingtrack.dto.request.CustomerRequest;
import com.g11.savingtrack.dto.response.CustomerResponse;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {
  CustomerResponse create (CustomerRequest request);
}
