import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService, SlotView } from '../services/api.service';
import { FormsModule } from '@angular/forms';

interface ZoneSummary {
  name: string;
  total: number;
  available: number;
  occupied: number;
  occupancyRate: number;
}

@Component({
  selector: 'app-lot-view',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './lot-view.component.html',
  styleUrls: ['./lot-view.component.css']
})
export class LotViewComponent implements OnInit, OnDestroy {
  floors: number[] = [];
  selectedFloor: number | null = null;
  selectedZone: string = '';
  searchTerm: string = '';
  slots: SlotView[] = [];
  filteredSlots: SlotView[] = [];
  selectedSlot: SlotView | null = null;
  intervalId: number | null = null;

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.apiService.getFloors().subscribe({
      next: floors => {
        this.floors = floors;
        this.selectedFloor = floors[0] ?? null;
        if (this.selectedFloor) {
          this.loadSlots();
          this.startPolling();
        }
      }
    });
  }

  ngOnDestroy() {
    if (this.intervalId) {
      window.clearInterval(this.intervalId);
    }
  }

  onFloorChange() {
    this.selectedZone = '';
    this.searchTerm = '';
    this.selectedSlot = null;
    this.loadSlots();
  }

  onZoneChange() {
    this.searchTerm = '';
    this.selectedSlot = null;
    this.applyFilters();
  }

  onSearchChange() {
    this.selectedZone = '';
    this.selectedSlot = null;
    this.applyFilters();
  }

  onSlotClick(slot: SlotView) {
    this.selectedSlot = this.selectedSlot?.slotId === slot.slotId ? null : slot;
  }

  isHighlighted(slot: SlotView): boolean {
    return this.selectedSlot?.slotId === slot.slotId;
  }

  getFilteredSlots(): SlotView[] {
    return this.filteredSlots;
  }

  getTotalSlots(): number {
    return this.slots.length;
  }

  getFreeSlots(): number {
    return this.slots.filter(s => s.status === 'FREE').length;
  }

  getOccupiedSlots(): number {
    return this.slots.filter(s => s.status === 'OCCUPIED').length;
  }

  getOccupancyRate(): number {
    const total = this.getTotalSlots();
    if (total === 0) return 0;
    return Math.round((this.getOccupiedSlots() / total) * 100);
  }

  getUniqueZones(): string[] {
    const zones = new Set(this.slots.map(s => s.zoneName));
    return Array.from(zones).sort();
  }

  getZoneSummary(): ZoneSummary[] {
    const zoneMap = new Map<string, ZoneSummary>();

    this.slots.forEach(slot => {
      if (!zoneMap.has(slot.zoneName)) {
        zoneMap.set(slot.zoneName, {
          name: slot.zoneName,
          total: 0,
          available: 0,
          occupied: 0,
          occupancyRate: 0
        });
      }

      const summary = zoneMap.get(slot.zoneName)!;
      summary.total++;

      if (slot.status === 'FREE') {
        summary.available++;
      } else {
        summary.occupied++;
      }
    });

    // Calculate occupancy rates
    zoneMap.forEach(summary => {
      summary.occupancyRate = summary.total > 0
        ? Math.round((summary.occupied / summary.total) * 100)
        : 0;
    });

    return Array.from(zoneMap.values()).sort((a, b) => a.name.localeCompare(b.name));
  }

  private startPolling() {
    this.intervalId = window.setInterval(() => this.loadSlots(), 2000);
  }

  private loadSlots() {
    if (!this.selectedFloor) {
      return;
    }
    this.apiService.getSlots(this.selectedFloor).subscribe({
      next: slots => {
        this.slots = slots;
        this.applyFilters();
      }
    });
  }

  private applyFilters() {
    let filtered = [...this.slots];

    // Apply zone filter
    if (this.selectedZone) {
      filtered = filtered.filter(s => s.zoneName === this.selectedZone);
    }

    // Apply search filter
    if (this.searchTerm.trim()) {
      const search = this.searchTerm.toLowerCase().trim();
      filtered = filtered.filter(s =>
        s.slotCode.toLowerCase().includes(search) ||
        s.zoneName.toLowerCase().includes(search)
      );
    }

    this.filteredSlots = filtered;
  }
}
