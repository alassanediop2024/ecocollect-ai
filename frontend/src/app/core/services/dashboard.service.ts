import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { DashboardStatistics } from '../models/dashboard-statistics.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl =
    `${environment.apiBaseUrl}/dashboard/statistics`;

  getStatistics(): Observable<DashboardStatistics> {
    return this.http.get<DashboardStatistics>(this.apiUrl);
  }
}
