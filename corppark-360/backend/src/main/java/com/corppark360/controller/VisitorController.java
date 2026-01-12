package com.corppark360.controller;

import com.corppark360.dto.EntryResponse;
import com.corppark360.dto.VisitorEntryRequest;
import com.corppark360.dto.VisitorInviteRequest;
import com.corppark360.dto.VisitorInviteResponse;
import com.corppark360.entity.VisitorInvite;
import com.corppark360.service.ParkingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/visitor")
public class VisitorController {
  private final ParkingService parkingService;

  public VisitorController(ParkingService parkingService) {
    this.parkingService = parkingService;
  }

  @PostMapping("/invite")
  public VisitorInviteResponse invite(@Valid @RequestBody VisitorInviteRequest request) {
    VisitorInvite invite = parkingService.createInvite(
        request.getHostEmpId(), request.getGuestName(), request.getGuestEmail());
    return new VisitorInviteResponse(invite.getInviteId(), invite.getQrToken(), invite.getValidUntil());
  }

  @PostMapping("/entry")
  public EntryResponse entry(@Valid @RequestBody VisitorEntryRequest request) {
    return parkingService.allocateForVisitor(request.getQrToken());
  }
}
