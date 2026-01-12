package com.corppark360.service;

import com.corppark360.dto.EmployeeCreateRequest;
import com.corppark360.entity.Employee;
import com.corppark360.entity.Role;
import com.corppark360.exception.ConflictException;
import com.corppark360.exception.NotFoundException;
import com.corppark360.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
  private final EmployeeRepository employeeRepository;

  public EmployeeService(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  public Employee getEmployee(String empId) {
    return employeeRepository
        .findById(empId)
        .orElseThrow(() -> new NotFoundException("Employee not found"));
  }

  public Employee createEmployee(EmployeeCreateRequest request) {
    if (employeeRepository.existsById(request.getEmpId())) {
      throw new ConflictException("Employee already exists");
    }
    Employee employee = new Employee();
    employee.setEmpId(request.getEmpId());
    employee.setName(request.getName());
    employee.setDept(request.getDept());
    employee.setVehicleType(request.getVehicleType());
    employee.setRole(Role.EMPLOYEE);
    return employeeRepository.save(employee);
  }
}
