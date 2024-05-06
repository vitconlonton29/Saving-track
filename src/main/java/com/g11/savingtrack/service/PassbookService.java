package com.g11.savingtrack.service;

import com.g11.savingtrack.dto.request.PassbookRequest;
import com.g11.savingtrack.dto.request.VerifyWithdrawalRequest;
import com.g11.savingtrack.dto.response.PassbookResponse;
import com.g11.savingtrack.dto.response.VerifyWithdrawalResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface PassbookService {
  PassbookResponse create(PassbookRequest request);

  List<PassbookResponse> list();
  PassbookResponse detail(int id);
  VerifyWithdrawalResponse withdraw(HttpServletRequest request, VerifyWithdrawalRequest verifyWithdrawalRequest, int passbookId);
}
