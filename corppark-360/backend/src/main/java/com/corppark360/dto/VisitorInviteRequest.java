package com.corppark360.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class VisitorInviteRequest {
  @NotBlank
  private String hostEmpId;

  @NotBlank
  private String guestName;

  @Email
  @NotBlank
  private String guestEmail;

  public String getHostEmpId() {
    return hostEmpId;
  }

  public void setHostEmpId(String hostEmpId) {
    this.hostEmpId = hostEmpId;
  }

  public String getGuestName() {
    return guestName;
  }

  public void setGuestName(String guestName) {
    this.guestName = guestName;
  }

  public String getGuestEmail() {
    return guestEmail;
  }

  public void setGuestEmail(String guestEmail) {
    this.guestEmail = guestEmail;
  }
}
