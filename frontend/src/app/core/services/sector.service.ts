import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import {
  CreateSectorRequest,
  Sector
} from '../models/sector.model';

import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SectorService {

  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiBaseUrl}/sectors`;

  getAll(): Observable<Sector[]> {
    return this.http.get<Sector[]>(this.apiUrl);
  }

  getById(id: number): Observable<Sector> {
    return this.http.get<Sector>(`${this.apiUrl}/${id}`);
  }

  create(request: CreateSectorRequest): Observable<Sector> {
    return this.http.post<Sector>(this.apiUrl, request);
  }
}
