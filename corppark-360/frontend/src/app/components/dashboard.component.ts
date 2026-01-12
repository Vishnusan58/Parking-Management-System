import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth.service';
import { ApiService, EntryResponse, MyCarResponse } from '../services/api.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent {
  myCar: MyCarResponse | null = null;
  allocation: EntryResponse | null = null;
  pillarId = '';
  message = '';
  error = '';

  constructor(private authService: AuthService, private apiService: ApiService) {
    this.refresh();
  }

  get empId() {
    return this.authService.empId();
  }

  refresh() {
    this.message = '';
    this.error = '';
    if (!this.empId) {
      return;
    }
    this.apiService.getMyCar(this.empId).subscribe({
      next: response => {
        this.myCar = response;
      },
      error: () => {
        this.myCar = null;
      }
    });
  }

  allocate() {
    this.message = '';
    this.error = '';
    if (!this.empId) {
      this.error = 'Please login first.';
      return;
    }
    this.apiService.allocate(this.empId).subscribe({
      next: response => {
        this.allocation = response;
        this.message = `Allocated ${response.slotCode} on floor ${response.floorNo}.`;
        this.refresh();
      },
      error: err => {
        this.error = err.error?.message || 'Unable to allocate slot.';
      }
    });
  }

  exit() {
    this.message = '';
    this.error = '';
    if (!this.empId) {
      this.error = 'Please login first.';
      return;
    }
    this.apiService.exit(this.empId).subscribe({
      next: () => {
        this.message = 'Exit recorded.';
        this.refresh();
      },
      error: err => {
        this.error = err.error?.message || 'Unable to exit.';
      }
    });
  }

  pin() {
    this.message = '';
    this.error = '';
    if (!this.empId || !this.pillarId.trim()) {
      this.error = 'Enter a pillar ID.';
      return;
    }
    this.apiService.pinPillar(this.empId, this.pillarId.trim()).subscribe({
      next: () => {
        this.message = 'Pillar pinned.';
        this.refresh();
      },
      error: err => {
        this.error = err.error?.message || 'Unable to pin pillar.';
      }
    });
  }
}
