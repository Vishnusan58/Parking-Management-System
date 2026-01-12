package com.corppark360.dto;

public class SlotViewResponse {
  private Integer slotId;
  private String slotCode;
  private String zoneName;
  private int floorNo;
  private String status;
  private boolean hasEvCharger;

  public SlotViewResponse(
      Integer slotId,
      String slotCode,
      String zoneName,
      int floorNo,
      String status,
      boolean hasEvCharger) {
    this.slotId = slotId;
    this.slotCode = slotCode;
    this.zoneName = zoneName;
    this.floorNo = floorNo;
    this.status = status;
    this.hasEvCharger = hasEvCharger;
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

  public int getFloorNo() {
    return floorNo;
  }

  public String getStatus() {
    return status;
  }

  public boolean isHasEvCharger() {
    return hasEvCharger;
  }
}
