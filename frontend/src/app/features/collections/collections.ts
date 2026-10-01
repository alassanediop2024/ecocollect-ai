import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import {
  Collection,
  CollectionStatus,
  CreateCollectionRequest
} from '../../core/models/collection.model';

import { Container } from '../../core/models/container.model';

import { CollectionService } from '../../core/services/collection.service';
import { ContainerService } from '../../core/services/container.service';

@Component({
  selector: 'app-collections',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './collections.html',
  styleUrl: './collections.scss'
})
export class Collections implements OnInit {

  private readonly collectionService = inject(CollectionService);
  private readonly containerService = inject(ContainerService);
  private readonly formBuilder = inject(FormBuilder);

  readonly collections = signal<Collection[]>([]);
  readonly containers = signal<Container[]>([]);

  readonly loading = signal(true);
  readonly saving = signal(false);
  readonly showForm = signal(false);

  readonly errorMessage = signal('');
  readonly formErrorMessage = signal('');
  readonly successMessage = signal('');

  readonly collectionForm = this.formBuilder.group({
    containerId: [
      null as number | null,
      [Validators.required]
    ],

    collectionDate: [
      '',
      [Validators.required]
    ],

    weightKg: [
      null as number | null,
      [Validators.min(0)]
    ],

    status: [
      'COMPLETED' as CollectionStatus
    ],

    operator: [
      '',
      [Validators.maxLength(150)]
    ],

    notes: [
      '',
      [Validators.maxLength(1000)]
    ]
  });

  ngOnInit(): void {
    this.loadCollections();
    this.loadContainers();
  }

  loadCollections(): void {
    this.loading.set(true);
    this.errorMessage.set('');

    this.collectionService.getAll().subscribe({
      next: (data) => {
        this.collections.set(data);
        this.loading.set(false);
      },

      error: () => {
        this.errorMessage.set(
          'Impossible de charger les collectes.'
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

    this.collectionForm.reset({
      containerId: null,
      collectionDate: this.getCurrentLocalDateTime(),
      weightKg: null,
      status: 'COMPLETED',
      operator: '',
      notes: ''
    });

    this.showForm.set(true);
  }

  closeForm(): void {
    this.showForm.set(false);
    this.formErrorMessage.set('');
  }

  submitCollection(): void {
    this.formErrorMessage.set('');
    this.successMessage.set('');

    if (this.collectionForm.invalid) {
      this.collectionForm.markAllAsTouched();

      this.formErrorMessage.set(
        'Veuillez corriger les champs du formulaire.'
      );

      return;
    }

    const formValue = this.collectionForm.getRawValue();

    if (
      formValue.containerId === null ||
      !formValue.collectionDate
    ) {
      this.formErrorMessage.set(
        'Le contenant et la date de collecte sont obligatoires.'
      );

      return;
    }

    const request: CreateCollectionRequest = {
      containerId: Number(formValue.containerId),
      collectionDate: formValue.collectionDate,
      weightKg:
        formValue.weightKg === null ||
        formValue.weightKg === undefined
          ? null
          : Number(formValue.weightKg),
      status: formValue.status ?? 'COMPLETED',
      operator:
        formValue.operator?.trim() || null,
      notes:
        formValue.notes?.trim() || null
    };

    this.saving.set(true);

    this.collectionService.create(request).subscribe({
      next: () => {
        this.saving.set(false);
        this.showForm.set(false);

        this.successMessage.set(
          'La collecte a été enregistrée avec succès.'
        );

        this.loadCollections();
      },

      error: (error) => {
        this.saving.set(false);

        if (error.status === 400) {
          this.formErrorMessage.set(
            'Les données de la collecte sont invalides.'
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
          'Une erreur est survenue lors de l’enregistrement de la collecte.'
        );
      }
    });
  }

  get completedCount(): number {
    return this.collections()
      .filter(collection => collection.status === 'COMPLETED')
      .length;
  }

  get totalWeightKg(): number {
    return this.collections()
      .reduce(
        (total, collection) =>
          total + (collection.weightKg ?? 0),
        0
      );
  }

  getStatusLabel(status: CollectionStatus | null): string {
    switch (status) {
      case 'SCHEDULED':
        return 'Planifiée';

      case 'IN_PROGRESS':
        return 'En cours';

      case 'COMPLETED':
        return 'Terminée';

      case 'CANCELLED':
        return 'Annulée';

      case 'FAILED':
        return 'Échec';

      default:
        return 'Non défini';
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
