import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiService, VisitorInviteResponse } from '../services/api.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-visitor-invite',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './visitor-invite.component.html'
})
export class VisitorInviteComponent {
  invite: VisitorInviteResponse | null = null;
  error = '';

  form = this.fb.group({
    guestName: ['', Validators.required],
    guestEmail: ['', [Validators.required, Validators.email]]
  });

  constructor(
    private fb: FormBuilder,
    private apiService: ApiService,
    private authService: AuthService
  ) {}

  submit() {
    this.error = '';
    const hostEmpId = this.authService.empId();
    if (!hostEmpId) {
      this.error = 'Please login first.';
      return;
    }
    const guestName = this.form.value.guestName?.trim();
    const guestEmail = this.form.value.guestEmail?.trim();
    if (!guestName || !guestEmail) {
      this.error = 'Enter guest details.';
      return;
    }
    this.apiService.inviteVisitor({ hostEmpId, guestName, guestEmail }).subscribe({
      next: invite => {
        this.invite = invite;
      },
      error: err => {
        this.error = err.error?.message || 'Unable to create invite.';
      }
    });
  }
}
