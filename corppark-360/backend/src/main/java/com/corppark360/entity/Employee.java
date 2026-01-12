package com.corppark360.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "employees")
public class Employee {
  @Id
  @Column(name = "emp_id", length = 20)
  private String empId;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false, length = 50)
  private String dept;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private Role role = Role.EMPLOYEE;

  @Enumerated(EnumType.STRING)
  @Column(name = "vehicle_type", nullable = false, length = 10)
  private VehicleType vehicleType;

  @Column(name = "sustainability_points")
  private int sustainabilityPoints;

  public String getEmpId() {
    return empId;
  }

  public void setEmpId(String empId) {
    this.empId = empId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDept() {
    return dept;
  }

  public void setDept(String dept) {
    this.dept = dept;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public VehicleType getVehicleType() {
    return vehicleType;
  }

  public void setVehicleType(VehicleType vehicleType) {
    this.vehicleType = vehicleType;
  }

  public int getSustainabilityPoints() {
    return sustainabilityPoints;
  }

  public void setSustainabilityPoints(int sustainabilityPoints) {
    this.sustainabilityPoints = sustainabilityPoints;
  }
}
