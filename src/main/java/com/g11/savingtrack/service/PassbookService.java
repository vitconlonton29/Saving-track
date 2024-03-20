package com.g11.savingtrack.service;

import com.g11.savingtrack.dto.request.PassbookRequest;
import com.g11.savingtrack.dto.response.InterestResponse;
import com.g11.savingtrack.dto.response.PassbookResponse;
import com.g11.savingtrack.entity.Passbook;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PassbookService {
  PassbookResponse create(PassbookRequest request);
  Passbook withdrawPassbook(int id);
  List<Passbook> getPassbooksByCccd(String cccd);
  PassbookResponse getPassbookById(int id);

  InterestResponse getInterest(int id);
}
