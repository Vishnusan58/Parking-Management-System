import { Injectable, signal } from '@angular/core';
import { ApiService, Employee } from './api.service';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private empIdSignal = signal<string | null>(localStorage.getItem('empId'));
  private employeeSignal = signal<Employee | null>(null);

  constructor(private apiService: ApiService) {
    const stored = this.empIdSignal();
    if (stored) {
      this.loadEmployee(stored).subscribe();
    }
  }

  empId() {
    return this.empIdSignal();
  }

  currentEmployee() {
    return this.employeeSignal();
  }

  isAdmin() {
    return this.employeeSignal()?.role === 'ADMIN';
  }

  setEmpId(empId: string) {
    localStorage.setItem('empId', empId);
    this.empIdSignal.set(empId);
    return this.loadEmployee(empId);
  }

  clear() {
    localStorage.removeItem('empId');
    this.empIdSignal.set(null);
    this.employeeSignal.set(null);
  }

  private loadEmployee(empId: string) {
    return this.apiService.getEmployee(empId).pipe(
      tap(employee => this.employeeSignal.set(employee))
    );
  }
}
