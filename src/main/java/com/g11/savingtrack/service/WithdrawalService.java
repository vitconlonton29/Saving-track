package com.g11.savingtrack.service;

import com.g11.savingtrack.dto.request.WithdrawalRequest;
import com.g11.savingtrack.dto.response.WithdrawalResponse;

public interface WithdrawalService {
  WithdrawalResponse withDraw(WithdrawalRequest request);

}
