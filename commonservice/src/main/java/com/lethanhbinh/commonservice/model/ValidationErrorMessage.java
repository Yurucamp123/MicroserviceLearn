package com.lethanhbinh.commonservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorMessage {
    private String code;
    private Map<String, String> errors;
    private HttpStatus status;
}
