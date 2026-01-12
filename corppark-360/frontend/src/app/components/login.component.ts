import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { ApiService } from '../services/api.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  errorMessage = '';
  infoMessage = '';

  form = this.fb.group({
    empId: ['', Validators.required]
  });

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private apiService: ApiService
  ) {}

  submit() {
    this.errorMessage = '';
    this.infoMessage = '';
    const empId = this.form.value.empId?.trim();
    if (!empId) {
      this.errorMessage = 'Enter a valid employee ID.';
      return;
    }
    this.authService.setEmpId(empId).subscribe({
      next: () => {
        this.router.navigate(['/dashboard']);
      },
      error: () => {
        this.errorMessage = 'Employee not found. You can register below.';
      }
    });
  }

  register() {
    this.errorMessage = '';
    this.infoMessage = '';
    const empId = this.form.value.empId?.trim();
    if (!empId) {
      this.errorMessage = 'Enter a valid employee ID.';
      return;
    }
    this.apiService.createEmployee({
      empId,
      name: `Employee ${empId}`,
      dept: 'General',
      vehicleType: 'CAR'
    }).subscribe({
      next: () => {
        this.infoMessage = 'Registration completed. Please log in again.';
      },
      error: () => {
        this.errorMessage = 'Unable to register employee.';
      }
    });
  }
}
