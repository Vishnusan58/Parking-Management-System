package com.corppark360.dto;

import java.time.LocalDateTime;

public class VisitorInviteResponse {
  private Long inviteId;
  private String qrToken;
  private LocalDateTime validUntil;

  public VisitorInviteResponse(Long inviteId, String qrToken, LocalDateTime validUntil) {
    this.inviteId = inviteId;
    this.qrToken = qrToken;
    this.validUntil = validUntil;
  }

  public Long getInviteId() {
    return inviteId;
  }

  public String getQrToken() {
    return qrToken;
  }

  public LocalDateTime getValidUntil() {
    return validUntil;
  }
}
