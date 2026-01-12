import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface EntryResponse {
  floorNo: number;
  slotId: number;
  slotCode: string;
  zoneName: string;
  reservationId: number;
}

export interface Employee {
  empId: string;
  name: string;
  dept: string;
  role: 'EMPLOYEE' | 'ADMIN';
  vehicleType: 'CAR' | 'BIKE' | 'EV';
}

export interface SlotView {
  slotId: number;
  slotCode: string;
  zoneName: string;
  floorNo: number;
  status: 'FREE' | 'OCCUPIED';
  hasEvCharger: boolean;
}

export interface MyCarResponse {
  reservationId: number;
  empId: string;
  slotId: number;
  slotCode: string;
  floorNo: number;
  zoneName: string;
  pinnedPillarId?: string;
  entryTs: string;
}

export interface VisitorInviteResponse {
  inviteId: number;
  qrToken: string;
  validUntil: string;
}

export interface Violation {
  violationId: number;
  reason: string;
  createdTs: string;
  employee: Employee;
  slot: {
    slotId: number;
    slotCode: string;
  };
}

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  allocate(empId: string): Observable<EntryResponse> {
    return this.http.post<EntryResponse>(`${this.baseUrl}/entry`, { empId });
  }

  exit(empId: string): Observable<{ status: string }> {
    return this.http.post<{ status: string }>(`${this.baseUrl}/exit`, { empId });
  }

  getMyCar(empId: string): Observable<MyCarResponse> {
    return this.http.get<MyCarResponse>(`${this.baseUrl}/mycar`, { params: { empId } });
  }

  pinPillar(empId: string, pillarId: string): Observable<{ status: string }> {
    return this.http.post<{ status: string }>(`${this.baseUrl}/mycar/pin`, { empId, pillarId });
  }

  getFloors(): Observable<number[]> {
    return this.http.get<number[]>(`${this.baseUrl}/floors`);
  }

  getSlots(floorNo: number): Observable<SlotView[]> {
    return this.http.get<SlotView[]>(`${this.baseUrl}/slots`, { params: { floorNo } });
  }

  getEmployee(empId: string): Observable<Employee> {
    return this.http.get<Employee>(`${this.baseUrl}/employees/${empId}`);
  }

  createEmployee(payload: { empId: string; name: string; dept: string; vehicleType: string }): Observable<Employee> {
    return this.http.post<Employee>(`${this.baseUrl}/employees`, payload);
  }

  inviteVisitor(payload: { hostEmpId: string; guestName: string; guestEmail: string }): Observable<VisitorInviteResponse> {
    return this.http.post<VisitorInviteResponse>(`${this.baseUrl}/visitor/invite`, payload);
  }

  createViolation(empId: string, payload: { empId: string; slotId: number; reason: string }): Observable<Violation> {
    const headers = new HttpHeaders({ 'X-EMP-ID': empId });
    return this.http.post<Violation>(`${this.baseUrl}/admin/violations`, payload, { headers });
  }

  listViolations(empId: string): Observable<Violation[]> {
    const headers = new HttpHeaders({ 'X-EMP-ID': empId });
    return this.http.get<Violation[]>(`${this.baseUrl}/admin/violations`, { headers });
  }
}
