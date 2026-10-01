import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { DashboardStatistics } from '../models/dashboard-statistics.model';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl =
    'http://localhost:8080/api/dashboard/statistics';

  getStatistics(): Observable<DashboardStatistics> {
    return this.http.get<DashboardStatistics>(this.apiUrl);
  }
}
