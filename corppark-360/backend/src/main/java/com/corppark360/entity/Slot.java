package com.corppark360.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "slots")
public class Slot {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "slot_id")
  private Integer slotId;

  @Column(name = "floor_no", nullable = false)
  private int floorNo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "zone_id", nullable = false)
  private Zone zone;

  @Column(name = "slot_code", unique = true, length = 20)
  private String slotCode;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private SlotStatus status = SlotStatus.FREE;

  @Column(name = "has_ev_charger")
  private boolean hasEvCharger;

  public Integer getSlotId() {
    return slotId;
  }

  public void setSlotId(Integer slotId) {
    this.slotId = slotId;
  }

  public int getFloorNo() {
    return floorNo;
  }

  public void setFloorNo(int floorNo) {
    this.floorNo = floorNo;
  }

  public Zone getZone() {
    return zone;
  }

  public void setZone(Zone zone) {
    this.zone = zone;
  }

  public String getSlotCode() {
    return slotCode;
  }

  public void setSlotCode(String slotCode) {
    this.slotCode = slotCode;
  }

  public SlotStatus getStatus() {
    return status;
  }

  public void setStatus(SlotStatus status) {
    this.status = status;
  }

  public boolean isHasEvCharger() {
    return hasEvCharger;
  }

  public void setHasEvCharger(boolean hasEvCharger) {
    this.hasEvCharger = hasEvCharger;
  }
}
