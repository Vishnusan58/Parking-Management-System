package com.corppark360.service;

import com.corppark360.dto.SlotViewResponse;
import com.corppark360.entity.Slot;
import com.corppark360.repository.SlotRepository;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class LotService {
  private final SlotRepository slotRepository;

  public LotService(SlotRepository slotRepository) {
    this.slotRepository = slotRepository;
  }

  public List<Integer> listFloors() {
    return slotRepository.findAll().stream()
        .map(Slot::getFloorNo)
        .distinct()
        .sorted()
        .toList();
  }

  public List<SlotViewResponse> listSlotsByFloor(int floorNo) {
    return slotRepository.findByFloorNoOrderBySlotId(floorNo).stream()
        .sorted(Comparator.comparing(Slot::getSlotId))
        .map(slot -> new SlotViewResponse(
            slot.getSlotId(),
            slot.getSlotCode(),
            slot.getZone().getZoneName(),
            slot.getFloorNo(),
            slot.getStatus().name(),
            slot.isHasEvCharger()))
        .collect(Collectors.toList());
  }
}
