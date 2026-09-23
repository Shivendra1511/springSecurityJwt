package com.example.securityPractice.controller;

import com.example.securityPractice.model.Employee;
import com.example.securityPractice.model.EmployeeDao;
import com.example.securityPractice.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@AllArgsConstructor
public class EmployeeController {
    private JwtEncoder jwtEncoder;
    private EmployeeService service;
    @GetMapping("/")
    public String greetings(){
        return "JEllo";
    }
    @PostMapping("/save-emp")
    public ResponseEntity<Employee> saveEmp(@RequestBody EmployeeDao employeeDao){
        return service.saveEmp(employeeDao);
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<Employee> findEmp(@PathVariable Long id){
        return service.find(id);
    }
    @PostMapping("/authentication")
    public String token(Authentication authentication){
        JwtClaimsSet claimsSet=JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60*30))
                .subject(authentication.getName())
                .build();
        JwtEncoderParameters parameters=JwtEncoderParameters.from(claimsSet);
        return jwtEncoder.encode(parameters).getTokenValue();
    }
}
