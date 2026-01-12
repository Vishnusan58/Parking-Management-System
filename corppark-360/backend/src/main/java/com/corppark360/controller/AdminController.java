package com.corppark360.controller;

import com.corppark360.dto.ViolationRequest;
import com.corppark360.entity.Violation;
import com.corppark360.service.ViolationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@Validated
public class AdminController {
  private final ViolationService violationService;

  public AdminController(ViolationService violationService) {
    this.violationService = violationService;
  }

  @PostMapping("/violations")
  public Violation createViolation(
      @RequestHeader("X-EMP-ID") @NotBlank String empId,
      @Valid @RequestBody ViolationRequest request) {
    return violationService.createViolation(empId, request);
  }

  @GetMapping("/violations")
  public List<Violation> listViolations(
      @RequestHeader("X-EMP-ID") @NotBlank String empId) {
    return violationService.listViolations(empId);
  }
}
