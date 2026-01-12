package com.corppark360.repository;

import com.corppark360.entity.Slot;
import com.corppark360.entity.SlotStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SlotRepository extends JpaRepository<Slot, Integer> {
  List<Slot> findByFloorNoOrderBySlotId(int floorNo);

  @Query(
      value = "SELECT * FROM slots WHERE status='FREE' AND zone_id IN (:zoneIds) "
          + "ORDER BY floor_no, slot_id LIMIT 1 FOR UPDATE SKIP LOCKED",
      nativeQuery = true)
  Slot findFirstAvailableSlotForUpdate(@Param("zoneIds") List<Integer> zoneIds);

  @Query(
      value = "SELECT * FROM slots WHERE status='FREE' AND has_ev_charger = TRUE "
          + "AND zone_id IN (:zoneIds) ORDER BY floor_no, slot_id LIMIT 1 FOR UPDATE SKIP LOCKED",
      nativeQuery = true)
  Slot findFirstEvSlotForUpdate(@Param("zoneIds") List<Integer> zoneIds);

  long countByStatus(SlotStatus status);
}
