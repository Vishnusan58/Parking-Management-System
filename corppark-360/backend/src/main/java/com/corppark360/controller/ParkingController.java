package com.corppark360.controller;

import com.corppark360.dto.EntryRequest;
import com.corppark360.dto.EntryResponse;
import com.corppark360.dto.ExitRequest;
import com.corppark360.dto.MyCarResponse;
import com.corppark360.dto.PinRequest;
import com.corppark360.dto.SlotViewResponse;
import com.corppark360.service.LotService;
import com.corppark360.service.ParkingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Validated
public class ParkingController {
  private final ParkingService parkingService;
  private final LotService lotService;

  public ParkingController(ParkingService parkingService, LotService lotService) {
    this.parkingService = parkingService;
    this.lotService = lotService;
  }

  @PostMapping("/entry")
  public EntryResponse allocate(@Valid @RequestBody EntryRequest request) {
    return parkingService.allocateForEmployee(request.getEmpId());
  }

  @PostMapping("/exit")
  public Map<String, String> exit(@Valid @RequestBody ExitRequest request) {
    parkingService.exit(request.getEmpId());
    return Map.of("status", "OK");
  }

  @GetMapping("/mycar")
  public MyCarResponse myCar(@RequestParam @NotBlank String empId) {
    return parkingService.findMyCar(empId);
  }

  @PostMapping("/mycar/pin")
  public Map<String, String> pin(@Valid @RequestBody PinRequest request) {
    parkingService.pinPillar(request.getEmpId(), request.getPillarId());
    return Map.of("status", "OK");
  }

  @GetMapping("/floors")
  public List<Integer> floors() {
    return lotService.listFloors();
  }

  @GetMapping("/slots")
  public List<SlotViewResponse> slots(@RequestParam @NotNull Integer floorNo) {
    return lotService.listSlotsByFloor(floorNo);
  }
}
