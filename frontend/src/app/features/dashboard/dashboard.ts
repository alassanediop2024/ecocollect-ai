import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DashboardService } from '../../core/services/dashboard.service';
import { DashboardStatistics } from '../../core/models/dashboard-statistics.model';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard implements OnInit {

  private readonly dashboardService = inject(DashboardService);

  readonly stats = signal<DashboardStatistics | null>(null);
  readonly loading = signal<boolean>(true);
  readonly errorMessage = signal<string>('');

  ngOnInit(): void {
    this.loadStatistics();
  }

  loadStatistics(): void {
    this.loading.set(true);
    this.errorMessage.set('');

    this.dashboardService.getStatistics().subscribe({
      next: (data: DashboardStatistics) => {
        this.stats.set(data);
        this.loading.set(false);
      },
      error: (error) => {
        console.error('Erreur de chargement du dashboard', error);
        this.errorMessage.set(
          'Impossible de charger les statistiques du tableau de bord.'
        );
        this.loading.set(false);
      }
    });
  }
}
