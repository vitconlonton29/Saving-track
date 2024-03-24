package com.g11.savingtrack.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyRequest {
    public String username;
    public String password;
    public String code;
    public String  identityCardNumber;
}
