package com.iso.spring3.core.models;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ApiError {
    private String message;
    private int code;
    private Map<String, String> errors = new HashMap<>();
}
