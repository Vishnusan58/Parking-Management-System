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
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "reservations",
    uniqueConstraints = {
      @UniqueConstraint(name = "uniq_active_emp", columnNames = {"emp_id", "active"}),
      @UniqueConstraint(name = "uniq_active_slot", columnNames = {"slot_id", "active"})
    }
)
public class Reservation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "res_id")
  private Long resId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "emp_id", nullable = false)
  private Employee employee;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "slot_id", nullable = false)
  private Slot slot;

  @Column(name = "entry_ts", nullable = false)
  private LocalDateTime entryTs = LocalDateTime.now();

  @Column(name = "exit_ts")
  private LocalDateTime exitTs;

  @Column(name = "pinned_pillar_id", length = 20)
  private String pinnedPillarId;

  @Column(nullable = false)
  private boolean active = true;

  public Long getResId() {
    return resId;
  }

  public void setResId(Long resId) {
    this.resId = resId;
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

  public LocalDateTime getEntryTs() {
    return entryTs;
  }

  public void setEntryTs(LocalDateTime entryTs) {
    this.entryTs = entryTs;
  }

  public LocalDateTime getExitTs() {
    return exitTs;
  }

  public void setExitTs(LocalDateTime exitTs) {
    this.exitTs = exitTs;
  }

  public String getPinnedPillarId() {
    return pinnedPillarId;
  }

  public void setPinnedPillarId(String pinnedPillarId) {
    this.pinnedPillarId = pinnedPillarId;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
