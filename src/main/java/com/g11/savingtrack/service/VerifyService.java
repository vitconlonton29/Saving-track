package com.g11.savingtrack.service;

import com.g11.savingtrack.dto.request.VerifyRequest;
import com.g11.savingtrack.dto.response.VerifyResponse;

public interface VerifyService {
    VerifyResponse verify(VerifyRequest verifyRequest);
}
