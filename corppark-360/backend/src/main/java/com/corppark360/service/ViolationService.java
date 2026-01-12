package com.corppark360.service;

import com.corppark360.dto.ViolationRequest;
import com.corppark360.entity.Employee;
import com.corppark360.entity.Role;
import com.corppark360.entity.Slot;
import com.corppark360.entity.Violation;
import com.corppark360.exception.NotFoundException;
import com.corppark360.exception.UnauthorizedException;
import com.corppark360.repository.EmployeeRepository;
import com.corppark360.repository.SlotRepository;
import com.corppark360.repository.ViolationRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ViolationService {
  private final EmployeeRepository employeeRepository;
  private final SlotRepository slotRepository;
  private final ViolationRepository violationRepository;

  public ViolationService(
      EmployeeRepository employeeRepository,
      SlotRepository slotRepository,
      ViolationRepository violationRepository) {
    this.employeeRepository = employeeRepository;
    this.slotRepository = slotRepository;
    this.violationRepository = violationRepository;
  }

  public Violation createViolation(String adminEmpId, ViolationRequest request) {
    Employee admin = employeeRepository
        .findById(adminEmpId)
        .orElseThrow(() -> new NotFoundException("Admin not found"));
    if (admin.getRole() != Role.ADMIN) {
      throw new UnauthorizedException("Admin access required");
    }

    Employee employee = employeeRepository
        .findById(request.getEmpId())
        .orElseThrow(() -> new NotFoundException("Employee not found"));
    Slot slot = slotRepository
        .findById(request.getSlotId())
        .orElseThrow(() -> new NotFoundException("Slot not found"));

    Violation violation = new Violation();
    violation.setEmployee(employee);
    violation.setSlot(slot);
    violation.setReason(request.getReason());
    return violationRepository.save(violation);
  }

  public List<Violation> listViolations(String adminEmpId) {
    Employee admin = employeeRepository
        .findById(adminEmpId)
        .orElseThrow(() -> new NotFoundException("Admin not found"));
    if (admin.getRole() != Role.ADMIN) {
      throw new UnauthorizedException("Admin access required");
    }
    return violationRepository.findAll();
  }
}
