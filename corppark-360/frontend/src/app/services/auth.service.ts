import { Injectable, signal } from '@angular/core';
import { ApiService, Employee } from './api.service';
import { EMPTY } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private empIdSignal = signal<string | null>(this.safeGetLocalStorage('empId'));
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
    this.safeSetLocalStorage('empId', empId);
    this.empIdSignal.set(empId);
    return this.loadEmployee(empId);
  }

  clear() {
    this.safeRemoveLocalStorage('empId');
    this.empIdSignal.set(null);
    this.employeeSignal.set(null);
  }

  private loadEmployee(empId: string) {
    return this.apiService.getEmployee(empId).pipe(
      tap(employee => this.employeeSignal.set(employee)),
      catchError(err => {
        this.employeeSignal.set(null);
        return EMPTY; // swallow errors to avoid breaking bootstrap
      })
    );
  }

  private safeGetLocalStorage(key: string): string | null {
    try {
      return localStorage.getItem(key);
    } catch {
      return null;
    }
  }

  private safeSetLocalStorage(key: string, value: string) {
    try {
      localStorage.setItem(key, value);
    } catch {
      // noop
    }
  }

  private safeRemoveLocalStorage(key: string) {
    try {
      localStorage.removeItem(key);
    } catch {
      // noop
    }
  }
}
