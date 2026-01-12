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
@Table(name = "violations")
public class Violation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "violation_id")
  private Long violationId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "emp_id")
  private Employee employee;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "slot_id")
  private Slot slot;

  @Column(length = 200)
  private String reason;

  @Column(name = "created_ts")
  private LocalDateTime createdTs = LocalDateTime.now();

  public Long getViolationId() {
    return violationId;
  }

  public void setViolationId(Long violationId) {
    this.violationId = violationId;
  }

  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  public Slot getSlot() {
    return slot;
  }

  public void setSlot(Slot slot) {
    this.slot = slot;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public LocalDateTime getCreatedTs() {
    return createdTs;
  }

  public void setCreatedTs(LocalDateTime createdTs) {
    this.createdTs = createdTs;
  }
}
