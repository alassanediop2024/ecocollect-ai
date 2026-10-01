import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import { ContainerService } from '../../core/services/container.service';
import { SectorService } from '../../core/services/sector.service';

import {
  Container,
  ContainerStatus,
  ContainerType,
  CreateContainerRequest
} from '../../core/models/container.model';

import { Sector } from '../../core/models/sector.model';

@Component({
  selector: 'app-containers',
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './containers.html',
  styleUrl: './containers.scss'
})
export class Containers implements OnInit {

  private readonly containerService = inject(ContainerService);
  private readonly sectorService = inject(SectorService);
  private readonly formBuilder = inject(FormBuilder);

  readonly containers = signal<Container[]>([]);
  readonly sectors = signal<Sector[]>([]);

  readonly loading = signal<boolean>(true);
  readonly saving = signal<boolean>(false);
  readonly showForm = signal<boolean>(false);

  readonly errorMessage = signal<string>('');
  readonly formErrorMessage = signal<string>('');
  readonly successMessage = signal<string>('');

  readonly containerForm = this.formBuilder.group({
    code: [
      '',
      [
        Validators.required,
        Validators.maxLength(50)
      ]
    ],
    address: [
      '',
      [
        Validators.required,
        Validators.maxLength(255)
      ]
    ],
    latitude: [
      null as number | null,
      [
        Validators.min(-90),
        Validators.max(90)
      ]
    ],
    longitude: [
      null as number | null,
      [
        Validators.min(-180),
        Validators.max(180)
      ]
    ],
    containerType: [
      'RECYCLING' as ContainerType,
      Validators.required
    ],
    capacity: [
      null as number | null,
      [
        Validators.required,
        Validators.min(1)
      ]
    ],
    status: [
      'ACTIVE' as ContainerStatus
    ],
    sectorId: [
      null as number | null,
      Validators.required
    ]
  });

  ngOnInit(): void {
    this.loadContainers();
    this.loadSectors();
  }

  loadContainers(): void {
    this.loading.set(true);
    this.errorMessage.set('');

    this.containerService.getAll().subscribe({
      next: (data) => {
        this.containers.set(data);
        this.loading.set(false);
      },
      error: (error) => {
        console.error('Erreur de chargement des contenants', error);
        this.errorMessage.set(
          'Impossible de charger les contenants.'
        );
        this.loading.set(false);
      }
    });
  }

  loadSectors(): void {
    this.sectorService.getAll().subscribe({
      next: (data) => {
        this.sectors.set(data);
      },
      error: (error) => {
        console.error('Erreur de chargement des secteurs', error);
        this.formErrorMessage.set(
          'Impossible de charger la liste des secteurs.'
        );
      }
    });
  }

  openForm(): void {
    this.successMessage.set('');
    this.formErrorMessage.set('');
    this.showForm.set(true);
  }

  closeForm(): void {
    this.showForm.set(false);
    this.formErrorMessage.set('');
    this.containerForm.reset({
      code: '',
      address: '',
      latitude: null,
      longitude: null,
      containerType: 'RECYCLING',
      capacity: null,
      status: 'ACTIVE',
      sectorId: null
    });
  }

  submitContainer(): void {
    this.formErrorMessage.set('');
    this.successMessage.set('');

    if (this.containerForm.invalid) {
      this.containerForm.markAllAsTouched();
      this.formErrorMessage.set(
        'Veuillez corriger les champs du formulaire.'
      );
      return;
    }

    const value = this.containerForm.getRawValue();

    const request: CreateContainerRequest = {
      code: value.code!,
      address: value.address!,
      latitude: value.latitude,
      longitude: value.longitude,
      containerType: value.containerType!,
      capacity: value.capacity!,
      status: value.status!,
      sectorId: value.sectorId!
    };

    this.saving.set(true);

    this.containerService.create(request).subscribe({
      next: () => {
        this.saving.set(false);
        this.closeForm();

        this.successMessage.set(
          'Le contenant a été créé avec succès.'
        );

        this.loadContainers();
      },
      error: (error) => {
        console.error('Erreur de création du contenant', error);

        this.saving.set(false);

        if (error.status === 409) {
          this.formErrorMessage.set(
            'Un contenant avec ce code existe déjà.'
          );
          return;
        }

        if (error.status === 400) {
          this.formErrorMessage.set(
            'Les données saisies sont invalides.'
          );
          return;
        }

        this.formErrorMessage.set(
          'Impossible de créer le contenant.'
        );
      }
    });
  }

  get activeCount(): number {
    return this.containers().filter(
      (container) => container.status === 'ACTIVE'
    ).length;
  }

  getTypeLabel(type: ContainerType): string {
    const labels: Record<ContainerType, string> = {
      GENERAL_WASTE: 'Déchets généraux',
      RECYCLING: 'Recyclage',
      ORGANIC: 'Matières organiques',
      GLASS: 'Verre',
      OTHER: 'Autre'
    };

    return labels[type];
  }

  getStatusLabel(status: ContainerStatus): string {
    const labels: Record<ContainerStatus, string> = {
      ACTIVE: 'Actif',
      INACTIVE: 'Inactif',
      MAINTENANCE: 'Maintenance',
      FULL: 'Plein',
      DAMAGED: 'Endommagé'
    };

    return labels[status];
  }
}
