package com.corppark360.dto;

import java.time.LocalDateTime;

public class MyCarResponse {
  private Long reservationId;
  private String empId;
  private Integer slotId;
  private String slotCode;
  private int floorNo;
  private String zoneName;
  private String pinnedPillarId;
  private LocalDateTime entryTs;

  public MyCarResponse(
      Long reservationId,
      String empId,
      Integer slotId,
      String slotCode,
      int floorNo,
      String zoneName,
      String pinnedPillarId,
      LocalDateTime entryTs) {
    this.reservationId = reservationId;
    this.empId = empId;
    this.slotId = slotId;
    this.slotCode = slotCode;
    this.floorNo = floorNo;
    this.zoneName = zoneName;
    this.pinnedPillarId = pinnedPillarId;
    this.entryTs = entryTs;
  }

  public Long getReservationId() {
    return reservationId;
  }

  public String getEmpId() {
    return empId;
  }

  public Integer getSlotId() {
    return slotId;
  }

  public String getSlotCode() {
    return slotCode;
  }

  public int getFloorNo() {
    return floorNo;
  }

  public String getZoneName() {
    return zoneName;
  }

  public String getPinnedPillarId() {
    return pinnedPillarId;
  }

  public LocalDateTime getEntryTs() {
    return entryTs;
  }
}
