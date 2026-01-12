import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService, SlotView } from '../services/api.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-lot-view',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './lot-view.component.html'
})
export class LotViewComponent implements OnInit, OnDestroy {
  floors: number[] = [];
  selectedFloor: number | null = null;
  slots: SlotView[] = [];
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
    this.loadSlots();
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
      }
    });
  }
}
