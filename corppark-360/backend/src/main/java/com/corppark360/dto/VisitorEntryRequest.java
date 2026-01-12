package com.corppark360.dto;

import jakarta.validation.constraints.NotBlank;

public class VisitorEntryRequest {
  @NotBlank
  private String qrToken;

  public String getQrToken() {
    return qrToken;
  }

  public void setQrToken(String qrToken) {
    this.qrToken = qrToken;
  }
}
