package com.corppark360.dto;

import jakarta.validation.constraints.NotBlank;

public class PinRequest {
  @NotBlank
  private String empId;

  @NotBlank
  private String pillarId;

  public String getEmpId() {
    return empId;
  }

  public void setEmpId(String empId) {
    this.empId = empId;
  }

  public String getPillarId() {
    return pillarId;
  }

  public void setPillarId(String pillarId) {
    this.pillarId = pillarId;
  }
}
