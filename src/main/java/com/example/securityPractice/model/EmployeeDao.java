package com.example.securityPractice.model;


public record EmployeeDao(
        String username,
        String password,
        String dept,
        Double salary
) {
}
