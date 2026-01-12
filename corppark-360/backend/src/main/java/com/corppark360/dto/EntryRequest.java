package com.corppark360.dto;

import jakarta.validation.constraints.NotBlank;

public class EntryRequest {
  @NotBlank
  private String empId;

  public String getEmpId() {
    return empId;
  }

  public void setEmpId(String empId) {
    this.empId = empId;
  }
}
