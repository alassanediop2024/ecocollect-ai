import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./features/dashboard/dashboard').then((m) => m.Dashboard),
    title: 'Tableau de bord | EcoCollect AI'
  },
  {
    path: 'containers',
    loadComponent: () =>
      import('./features/containers/containers').then((m) => m.Containers),
    title: 'Contenants | EcoCollect AI'
  },
  {
    path: 'collections',
    loadComponent: () =>
      import('./features/collections/collections').then((m) => m.Collections),
    title: 'Collectes | EcoCollect AI'
  },
  {
    path: 'anomalies',
    loadComponent: () =>
      import('./features/anomalies/anomalies').then((m) => m.Anomalies),
    title: 'Anomalies | EcoCollect AI'
  },
  {
    path: 'sectors',
    loadComponent: () =>
      import('./features/sectors/sectors').then((m) => m.Sectors),
    title: 'Secteurs | EcoCollect AI'
  },
  {
    path: '**',
    redirectTo: ''
  }
];
