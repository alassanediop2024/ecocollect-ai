import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import {
  Anomaly,
  CreateAnomalyRequest
} from '../models/anomaly.model';

@Injectable({
  providedIn: 'root'
})
export class AnomalyService {

  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/anomalies';

  getAll(): Observable<Anomaly[]> {
    return this.http.get<Anomaly[]>(this.apiUrl);
  }

  getById(id: number): Observable<Anomaly> {
    return this.http.get<Anomaly>(`${this.apiUrl}/${id}`);
  }

  create(request: CreateAnomalyRequest): Observable<Anomaly> {
    return this.http.post<Anomaly>(this.apiUrl, request);
  }

  resolve(id: number): Observable<Anomaly> {
    return this.http.put<Anomaly>(
      `${this.apiUrl}/${id}/resolve`,
      {}
    );
  }
}
