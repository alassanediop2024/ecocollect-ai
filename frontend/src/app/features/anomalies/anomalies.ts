import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import {
  Anomaly,
  AnomalySeverity,
  AnomalyStatus,
  AnomalyType,
  CreateAnomalyRequest
} from '../../core/models/anomaly.model';

import { Container } from '../../core/models/container.model';

import { AnomalyService } from '../../core/services/anomaly.service';
import { ContainerService } from '../../core/services/container.service';

@Component({
  selector: 'app-anomalies',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './anomalies.html',
  styleUrl: './anomalies.scss'
})
export class Anomalies implements OnInit {

  private readonly anomalyService = inject(AnomalyService);
  private readonly containerService = inject(ContainerService);
  private readonly formBuilder = inject(FormBuilder);

  readonly anomalies = signal<Anomaly[]>([]);
  readonly containers = signal<Container[]>([]);

  readonly loading = signal(true);
  readonly saving = signal(false);
  readonly resolvingId = signal<number | null>(null);
  readonly showForm = signal(false);

  readonly errorMessage = signal('');
  readonly formErrorMessage = signal('');
  readonly successMessage = signal('');

  readonly anomalyForm = this.formBuilder.group({
    containerId: [
      null as number | null,
      [Validators.required]
    ],

    type: [
      'OVERFLOW' as AnomalyType,
      [Validators.required]
    ],

    description: [
      '',
      [
        Validators.required,
        Validators.maxLength(1000)
      ]
    ],

    severity: [
      'MEDIUM' as AnomalySeverity,
      [Validators.required]
    ],

    status: [
      'REPORTED' as AnomalyStatus
    ],

    reportedAt: [
      '',
      [Validators.required]
    ]
  });

  ngOnInit(): void {
    this.loadAnomalies();
    this.loadContainers();
  }

  loadAnomalies(): void {
    this.loading.set(true);
    this.errorMessage.set('');

    this.anomalyService.getAll().subscribe({
      next: (data) => {
        this.anomalies.set(data);
        this.loading.set(false);
      },

      error: () => {
        this.errorMessage.set(
          'Impossible de charger les anomalies.'
        );
        this.loading.set(false);
      }
    });
  }

  loadContainers(): void {
    this.containerService.getAll().subscribe({
      next: (data) => {
        this.containers.set(data);
      },

      error: () => {
        this.formErrorMessage.set(
          'Impossible de charger la liste des contenants.'
        );
      }
    });
  }

  openForm(): void {
    this.successMessage.set('');
    this.formErrorMessage.set('');

    this.anomalyForm.reset({
      containerId: null,
      type: 'OVERFLOW',
      description: '',
      severity: 'MEDIUM',
      status: 'REPORTED',
      reportedAt: this.getCurrentLocalDateTime()
    });

    this.showForm.set(true);
  }

  closeForm(): void {
    this.showForm.set(false);
    this.formErrorMessage.set('');
  }

  submitAnomaly(): void {
    this.formErrorMessage.set('');
    this.successMessage.set('');

    if (this.anomalyForm.invalid) {
      this.anomalyForm.markAllAsTouched();

      this.formErrorMessage.set(
        'Veuillez corriger les champs du formulaire.'
      );

      return;
    }

    const formValue = this.anomalyForm.getRawValue();

    if (
      formValue.containerId === null ||
      !formValue.type ||
      !formValue.description?.trim() ||
      !formValue.severity
    ) {
      this.formErrorMessage.set(
        'Les champs obligatoires doivent être renseignés.'
      );

      return;
    }

    const request: CreateAnomalyRequest = {
      containerId: Number(formValue.containerId),
      type: formValue.type,
      description: formValue.description.trim(),
      severity: formValue.severity,
      status: formValue.status ?? 'REPORTED',
      reportedAt: formValue.reportedAt || null
    };

    this.saving.set(true);

    this.anomalyService.create(request).subscribe({
      next: () => {
        this.saving.set(false);
        this.showForm.set(false);

        this.successMessage.set(
          'L’anomalie a été signalée avec succès.'
        );

        this.loadAnomalies();
      },

      error: (error) => {
        this.saving.set(false);

        if (error.status === 400) {
          this.formErrorMessage.set(
            'Les données de l’anomalie sont invalides.'
          );
          return;
        }

        if (error.status === 404) {
          this.formErrorMessage.set(
            'Le contenant sélectionné est introuvable.'
          );
          return;
        }

        this.formErrorMessage.set(
          'Une erreur est survenue lors de l’enregistrement.'
        );
      }
    });
  }

  resolveAnomaly(anomaly: Anomaly): void {
    if (
      anomaly.status === 'RESOLVED' ||
      anomaly.status === 'CLOSED'
    ) {
      return;
    }

    this.successMessage.set('');
    this.errorMessage.set('');
    this.resolvingId.set(anomaly.id);

    this.anomalyService.resolve(anomaly.id).subscribe({
      next: () => {
        this.resolvingId.set(null);

        this.successMessage.set(
          `L’anomalie #${anomaly.id} a été résolue.`
        );

        this.loadAnomalies();
      },

      error: () => {
        this.resolvingId.set(null);

        this.errorMessage.set(
          `Impossible de résoudre l’anomalie #${anomaly.id}.`
        );
      }
    });
  }

  get openCount(): number {
    return this.anomalies()
      .filter(
        anomaly =>
          anomaly.status === 'REPORTED' ||
          anomaly.status === 'IN_PROGRESS'
      )
      .length;
  }

  get criticalCount(): number {
    return this.anomalies()
      .filter(
        anomaly =>
          anomaly.severity === 'CRITICAL' &&
          anomaly.status !== 'RESOLVED' &&
          anomaly.status !== 'CLOSED'
      )
      .length;
  }

  get resolvedCount(): number {
    return this.anomalies()
      .filter(
        anomaly =>
          anomaly.status === 'RESOLVED' ||
          anomaly.status === 'CLOSED'
      )
      .length;
  }

  getTypeLabel(type: AnomalyType): string {
    switch (type) {
      case 'OVERFLOW':
        return 'Débordement';

      case 'DAMAGED':
        return 'Contenant endommagé';

      case 'ACCESS_BLOCKED':
        return 'Accès bloqué';

      case 'ILLEGAL_DUMPING':
        return 'Dépôt sauvage';

      case 'SENSOR_FAILURE':
        return 'Défaillance capteur';

      case 'OTHER':
        return 'Autre';
    }
  }

  getSeverityLabel(severity: AnomalySeverity): string {
    switch (severity) {
      case 'LOW':
        return 'Faible';

      case 'MEDIUM':
        return 'Moyenne';

      case 'HIGH':
        return 'Élevée';

      case 'CRITICAL':
        return 'Critique';
    }
  }

  getStatusLabel(status: AnomalyStatus): string {
    switch (status) {
      case 'REPORTED':
        return 'Signalée';

      case 'IN_PROGRESS':
        return 'En cours';

      case 'RESOLVED':
        return 'Résolue';

      case 'CLOSED':
        return 'Fermée';
    }
  }

  private getCurrentLocalDateTime(): string {
    const now = new Date();
    const offset = now.getTimezoneOffset() * 60000;

    return new Date(now.getTime() - offset)
      .toISOString()
      .slice(0, 16);
  }
}
