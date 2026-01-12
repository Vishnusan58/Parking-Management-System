package com.corppark360.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ViolationRequest {
  @NotBlank
  private String empId;

  @NotNull
  private Integer slotId;

  @NotBlank
  private String reason;

  public String getEmpId() {
    return empId;
  }

  public void setEmpId(String empId) {
    this.empId = empId;
  }

  public Integer getSlotId() {
    return slotId;
  }

  public void setSlotId(Integer slotId) {
    this.slotId = slotId;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }
}
