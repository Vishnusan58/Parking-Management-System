import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService, Violation } from '../services/api.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin.component.html'
})
export class AdminComponent implements OnInit {
  violations: Violation[] = [];
  empId = '';
  slotId: number | null = null;
  reason = '';
  error = '';
  message = '';

  constructor(private apiService: ApiService, private authService: AuthService) {}

  ngOnInit() {
    this.loadViolations();
  }

  get isAdmin() {
    return this.authService.isAdmin();
  }

  loadViolations() {
    this.error = '';
    const adminId = this.authService.empId();
    if (!adminId) {
      this.error = 'Please login first.';
      return;
    }
    this.apiService.listViolations(adminId).subscribe({
      next: violations => {
        this.violations = violations;
      },
      error: err => {
        this.error = err.error?.message || 'Unable to load violations.';
      }
    });
  }

  createViolation() {
    this.error = '';
    this.message = '';
    const adminId = this.authService.empId();
    if (!adminId) {
      this.error = 'Please login first.';
      return;
    }
    if (!this.empId || !this.slotId || !this.reason) {
      this.error = 'Fill all fields.';
      return;
    }
    this.apiService.createViolation(adminId, {
      empId: this.empId,
      slotId: this.slotId,
      reason: this.reason
    }).subscribe({
      next: () => {
        this.message = 'Violation recorded.';
        this.empId = '';
        this.slotId = null;
        this.reason = '';
        this.loadViolations();
      },
      error: err => {
        this.error = err.error?.message || 'Unable to create violation.';
      }
    });
  }
}
