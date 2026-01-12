package com.corppark360.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "zones")
public class Zone {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "zone_id")
  private Integer zoneId;

  @Column(name = "zone_name", nullable = false, length = 50)
  private String zoneName;

  @Enumerated(EnumType.STRING)
  @Column(name = "access_level", nullable = false, length = 10)
  private AccessLevel accessLevel;

  public Integer getZoneId() {
    return zoneId;
  }

  public void setZoneId(Integer zoneId) {
    this.zoneId = zoneId;
  }

  public String getZoneName() {
    return zoneName;
  }

  public void setZoneName(String zoneName) {
    this.zoneName = zoneName;
  }

  public AccessLevel getAccessLevel() {
    return accessLevel;
  }

  public void setAccessLevel(AccessLevel accessLevel) {
    this.accessLevel = accessLevel;
  }
}
