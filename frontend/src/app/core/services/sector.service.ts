import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Sector } from '../models/sector.model';

@Injectable({
  providedIn: 'root'
})
export class SectorService {

  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/sectors';

  getAll(): Observable<Sector[]> {
    return this.http.get<Sector[]>(this.apiUrl);
  }
}
