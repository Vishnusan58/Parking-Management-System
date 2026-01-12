package com.corppark360.repository;

import com.corppark360.entity.Reservation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
  @Query("select r from Reservation r where r.employee.empId = :empId and r.active = true")
  Optional<Reservation> findActiveByEmpId(@Param("empId") String empId);
}
