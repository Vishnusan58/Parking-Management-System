package com.corppark360.controller;

import com.corppark360.dto.EmployeeCreateRequest;
import com.corppark360.entity.Employee;
import com.corppark360.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
  private final EmployeeService employeeService;

  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @GetMapping("/{empId}")
  public Employee getEmployee(@PathVariable String empId) {
    return employeeService.getEmployee(empId);
  }

  @PostMapping
  public Employee createEmployee(@Valid @RequestBody EmployeeCreateRequest request) {
    return employeeService.createEmployee(request);
  }
}
