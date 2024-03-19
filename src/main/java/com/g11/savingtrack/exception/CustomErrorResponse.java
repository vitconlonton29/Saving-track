package com.g11.savingtrack.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@Data
@NoArgsConstructor
public class CustomErrorResponse {
    private String code;
    private int status;
    private Map<String, String> params;
    private String message;
    private String path;
    private String trace;
    // getters and setters
}
