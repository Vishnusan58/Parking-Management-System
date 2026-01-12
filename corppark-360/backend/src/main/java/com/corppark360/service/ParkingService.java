package com.corppark360.service;

import com.corppark360.dto.EntryResponse;
import com.corppark360.dto.MyCarResponse;
import com.corppark360.entity.AccessLevel;
import com.corppark360.entity.Employee;
import com.corppark360.entity.Reservation;
import com.corppark360.entity.Role;
import com.corppark360.entity.Slot;
import com.corppark360.entity.SlotStatus;
import com.corppark360.entity.VehicleType;
import com.corppark360.entity.Zone;
import com.corppark360.entity.VisitorInvite;
import com.corppark360.exception.BadRequestException;
import com.corppark360.exception.ConflictException;
import com.corppark360.exception.NotFoundException;
import com.corppark360.repository.EmployeeRepository;
import com.corppark360.repository.ReservationRepository;
import com.corppark360.repository.SlotRepository;
import com.corppark360.repository.ZoneRepository;
import com.corppark360.repository.VisitorInviteRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParkingService {
  private final EmployeeRepository employeeRepository;
  private final ZoneRepository zoneRepository;
  private final SlotRepository slotRepository;
  private final ReservationRepository reservationRepository;
  private final VisitorInviteRepository visitorInviteRepository;

  public ParkingService(
      EmployeeRepository employeeRepository,
      ZoneRepository zoneRepository,
      SlotRepository slotRepository,
      ReservationRepository reservationRepository,
      VisitorInviteRepository visitorInviteRepository) {
    this.employeeRepository = employeeRepository;
    this.zoneRepository = zoneRepository;
    this.slotRepository = slotRepository;
    this.reservationRepository = reservationRepository;
    this.visitorInviteRepository = visitorInviteRepository;
  }

  @Transactional
  public EntryResponse allocateForEmployee(String empId) {
    Employee employee = employeeRepository
        .findById(empId)
        .orElseThrow(() -> new NotFoundException("Employee not found"));

    reservationRepository
        .findActiveByEmpId(empId)
        .ifPresent(reservation -> {
          throw new ConflictException("Active reservation exists");
        });

    List<Integer> evZoneIds = zoneIdsByAccessLevel(AccessLevel.EV);
    List<Integer> seniorZoneIds = zoneIdsByAccessLevel(AccessLevel.SENIOR);
    List<Integer> juniorZoneIds = zoneIdsByAccessLevel(AccessLevel.JUNIOR);

    Slot slot = null;
    if (employee.getVehicleType() == VehicleType.EV) {
      if (!evZoneIds.isEmpty()) {
        slot = slotRepository.findFirstAvailableSlotForUpdate(evZoneIds);
      }
      List<Integer> eligibleZoneIds = new ArrayList<>();
      eligibleZoneIds.addAll(evZoneIds);
      eligibleZoneIds.addAll(seniorZoneIds);
      eligibleZoneIds.addAll(juniorZoneIds);
      if (slot == null && !eligibleZoneIds.isEmpty()) {
        slot = slotRepository.findFirstEvSlotForUpdate(eligibleZoneIds);
      }
      if (slot == null && !eligibleZoneIds.isEmpty()) {
        slot = slotRepository.findFirstAvailableSlotForUpdate(eligibleZoneIds);
      }
    } else if (employee.getRole() == Role.ADMIN || "Management".equalsIgnoreCase(employee.getDept())) {
      slot = tryAllocateInOrder(seniorZoneIds, juniorZoneIds);
    } else {
      slot = tryAllocateInOrder(juniorZoneIds);
    }

    if (slot == null) {
      throw new ConflictException("Lot full for your eligibility");
    }

    slot.setStatus(SlotStatus.OCCUPIED);
    Reservation reservation = new Reservation();
    reservation.setEmployee(employee);
    reservation.setSlot(slot);
    reservation.setEntryTs(LocalDateTime.now());
    reservation.setActive(true);

    slotRepository.save(slot);
    Reservation saved = reservationRepository.save(reservation);

    return new EntryResponse(
        slot.getFloorNo(),
        slot.getSlotId(),
        slot.getSlotCode(),
        slot.getZone().getZoneName(),
        saved.getResId());
  }

  @Transactional
  public EntryResponse allocateForVisitor(String qrToken) {
    VisitorInvite invite = visitorInviteRepository
        .findByQrToken(qrToken)
        .orElseThrow(() -> new NotFoundException("Invite not found"));
    if (invite.isUsed()) {
      throw new ConflictException("Invite already used");
    }
    if (invite.getValidUntil() != null && invite.getValidUntil().isBefore(LocalDateTime.now())) {
      throw new BadRequestException("Invite expired");
    }

    List<Integer> visitorZoneIds = zoneIdsByAccessLevel(AccessLevel.EV);
    if (visitorZoneIds.isEmpty()) {
      visitorZoneIds = zoneIdsByAccessLevel(AccessLevel.VISITOR);
    }
    if (visitorZoneIds.isEmpty()) {
      throw new ConflictException("Lot full for your eligibility");
    }
    Slot slot = slotRepository.findFirstAvailableSlotForUpdate(visitorZoneIds);
    if (slot == null) {
      throw new ConflictException("Lot full for your eligibility");
    }

    Employee visitor = employeeRepository.findById(visitorEmpId(invite)).orElseGet(() -> {
      Employee created = new Employee();
      created.setEmpId(visitorEmpId(invite));
      created.setName(Optional.ofNullable(invite.getGuestName()).orElse("Visitor"));
      created.setDept("Visitor");
      created.setVehicleType(VehicleType.CAR);
      created.setRole(Role.EMPLOYEE);
      return employeeRepository.save(created);
    });

    Reservation reservation = new Reservation();
    reservation.setEmployee(visitor);
    reservation.setSlot(slot);
    reservation.setEntryTs(LocalDateTime.now());
    reservation.setActive(true);

    slot.setStatus(SlotStatus.OCCUPIED);
    slotRepository.save(slot);
    Reservation saved = reservationRepository.save(reservation);

    invite.setUsed(true);
    visitorInviteRepository.save(invite);

    return new EntryResponse(
        slot.getFloorNo(),
        slot.getSlotId(),
        slot.getSlotCode(),
        slot.getZone().getZoneName(),
        saved.getResId());
  }

  @Transactional
  public void exit(String empId) {
    Reservation reservation = reservationRepository
        .findActiveByEmpId(empId)
        .orElseThrow(() -> new NotFoundException("Active reservation not found"));
    reservation.setExitTs(LocalDateTime.now());
    reservation.setActive(false);
    Slot slot = reservation.getSlot();
    slot.setStatus(SlotStatus.FREE);
    slotRepository.save(slot);
    reservationRepository.save(reservation);
  }

  public MyCarResponse findMyCar(String empId) {
    Reservation reservation = reservationRepository
        .findActiveByEmpId(empId)
        .orElseThrow(() -> new NotFoundException("Active reservation not found"));
    Slot slot = reservation.getSlot();
    return new MyCarResponse(
        reservation.getResId(),
        reservation.getEmployee().getEmpId(),
        slot.getSlotId(),
        slot.getSlotCode(),
        slot.getFloorNo(),
        slot.getZone().getZoneName(),
        reservation.getPinnedPillarId(),
        reservation.getEntryTs());
  }

  @Transactional
  public void pinPillar(String empId, String pillarId) {
    Reservation reservation = reservationRepository
        .findActiveByEmpId(empId)
        .orElseThrow(() -> new NotFoundException("Active reservation not found"));
    reservation.setPinnedPillarId(pillarId);
    reservationRepository.save(reservation);
  }

  @Transactional
  public VisitorInvite createInvite(String hostEmpId, String guestName, String guestEmail) {
    Employee host = employeeRepository
        .findById(hostEmpId)
        .orElseThrow(() -> new NotFoundException("Host employee not found"));
    VisitorInvite invite = new VisitorInvite();
    invite.setHostEmployee(host);
    invite.setGuestName(guestName);
    invite.setGuestEmail(guestEmail);
    invite.setQrToken(UUID.randomUUID().toString().replace("-", ""));
    invite.setValidUntil(LocalDateTime.now().plusHours(24));
    invite.setUsed(false);
    return visitorInviteRepository.save(invite);
  }

  private Slot tryAllocateInOrder(List<Integer>... zoneLists) {
    for (List<Integer> zoneIds : zoneLists) {
      if (!zoneIds.isEmpty()) {
        Slot slot = slotRepository.findFirstAvailableSlotForUpdate(zoneIds);
        if (slot != null) {
          return slot;
        }
      }
    }
    return null;
  }

  private List<Integer> zoneIdsByAccessLevel(AccessLevel accessLevel) {
    return zoneRepository.findByAccessLevel(accessLevel).stream()
        .filter(Objects::nonNull)
        .map(Zone::getZoneId)
        .toList();
  }

  private String visitorEmpId(VisitorInvite invite) {
    return "V-" + invite.getInviteId();
  }
}
