package com.example.securityPractice.service;

import com.example.securityPractice.exception.UserNotFoundException;
import com.example.securityPractice.model.Employee;
import com.example.securityPractice.model.EmployeeDao;
import com.example.securityPractice.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Service
@AllArgsConstructor
public class EmployeeService {
    private EmployeeRepository repository;
    private PasswordEncoder passwordEncoder;
    public ResponseEntity<Employee> saveEmp(EmployeeDao employeeDao) {
        Employee employee=new Employee();
        employee.setUsername(employeeDao.username());
        employee.setPassword(passwordEncoder.encode(employeeDao.password()));
        employee.setDept(employeeDao.dept());
        employee.setSalary(employeeDao.salary());
        Employee saved=repository.save(employee);
        URI location= ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/find/{id}")
                .buildAndExpand(saved.getEmp_id())
                .toUri();
        return ResponseEntity.created(location).body(saved);
    }
    public ResponseEntity<Employee> find(Long id) {
        Employee employee= repository.findById(id).orElseThrow(()->new UserNotFoundException("No such Employee Of id " + id));
        return new ResponseEntity<>(employee, HttpStatus.FOUND);
    }
}
