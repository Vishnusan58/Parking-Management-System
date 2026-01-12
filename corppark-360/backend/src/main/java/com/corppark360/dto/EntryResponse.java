package com.corppark360.dto;

public class EntryResponse {
  private int floorNo;
  private Integer slotId;
  private String slotCode;
  private String zoneName;
  private Long reservationId;

  public EntryResponse(int floorNo, Integer slotId, String slotCode, String zoneName, Long reservationId) {
    this.floorNo = floorNo;
    this.slotId = slotId;
    this.slotCode = slotCode;
    this.zoneName = zoneName;
    this.reservationId = reservationId;
  }

  public int getFloorNo() {
    return floorNo;
  }

  public Integer getSlotId() {
    return slotId;
  }

  public String getSlotCode() {
    return slotCode;
  }

  public String getZoneName() {
    return zoneName;
  }

  public Long getReservationId() {
    return reservationId;
  }
}
