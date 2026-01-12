package com.corppark360.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "visitor_invites")
public class VisitorInvite {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "invite_id")
  private Long inviteId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "host_emp_id")
  private Employee hostEmployee;

  @Column(name = "guest_name", length = 100)
  private String guestName;

  @Column(name = "guest_email", length = 150)
  private String guestEmail;

  @Column(name = "qr_token", length = 64, unique = true)
  private String qrToken;

  @Column(name = "valid_until")
  private LocalDateTime validUntil;

  @Column
  private boolean used = false;

  public Long getInviteId() {
    return inviteId;
  }

  public void setInviteId(Long inviteId) {
    this.inviteId = inviteId;
  }

  public Employee getHostEmployee() {
    return hostEmployee;
  }

  public void setHostEmployee(Employee hostEmployee) {
    this.hostEmployee = hostEmployee;
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

  public String getQrToken() {
    return qrToken;
  }

  public void setQrToken(String qrToken) {
    this.qrToken = qrToken;
  }

  public LocalDateTime getValidUntil() {
    return validUntil;
  }

  public void setValidUntil(LocalDateTime validUntil) {
    this.validUntil = validUntil;
  }

  public boolean isUsed() {
    return used;
  }

  public void setUsed(boolean used) {
    this.used = used;
  }
}
