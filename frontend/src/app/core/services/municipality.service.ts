import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Municipality } from '../models/municipality.model';

@Injectable({
  providedIn: 'root'
})
export class MunicipalityService {

  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/municipalities';

  getAll(): Observable<Municipality[]> {
    return this.http.get<Municipality[]>(this.apiUrl);
  }

  getById(id: number): Observable<Municipality> {
    return this.http.get<Municipality>(`${this.apiUrl}/${id}`);
  }
}
