package com.example.securityPractice.configuration;

import com.example.securityPractice.model.Employee;
import com.example.securityPractice.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private EmployeeRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee=repository.findByusername(username);
        if(employee==null){
            throw new UsernameNotFoundException("No Such User");
        }
        return User.withUsername(employee.getUsername())
                .password(employee.getPassword())
                .build();
    }
}
