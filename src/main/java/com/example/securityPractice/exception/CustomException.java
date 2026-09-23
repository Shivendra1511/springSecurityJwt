package com.example.securityPractice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CustomException {
    private LocalDateTime localDateTime;
    private String message;
    private String details;
}
