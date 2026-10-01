import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import {
  CreateSectorRequest,
  Sector
} from '../../core/models/sector.model';

import { Municipality } from '../../core/models/municipality.model';

import { SectorService } from '../../core/services/sector.service';
import { MunicipalityService } from '../../core/services/municipality.service';

@Component({
  selector: 'app-sectors',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './sectors.html',
  styleUrl: './sectors.scss'
})
export class Sectors implements OnInit {

  private readonly sectorService = inject(SectorService);
  private readonly municipalityService = inject(MunicipalityService);
  private readonly formBuilder = inject(FormBuilder);

  readonly sectors = signal<Sector[]>([]);
  readonly municipalities = signal<Municipality[]>([]);

  readonly loading = signal(true);
  readonly saving = signal(false);
  readonly showForm = signal(false);

  readonly errorMessage = signal('');
  readonly formErrorMessage = signal('');
  readonly successMessage = signal('');

  readonly sectorForm = this.formBuilder.group({
    code: [
      '',
      [
        Validators.required,
        Validators.maxLength(50)
      ]
    ],

    name: [
      '',
      [
        Validators.required,
        Validators.maxLength(150)
      ]
    ],

    description: [
      '',
      [
        Validators.maxLength(500)
      ]
    ],

    municipalityId: [
      null as number | null,
      [Validators.required]
    ]
  });

  ngOnInit(): void {
    this.loadSectors();
    this.loadMunicipalities();
  }

  loadSectors(): void {
    this.loading.set(true);
    this.errorMessage.set('');

    this.sectorService.getAll().subscribe({
      next: (data) => {
        this.sectors.set(data);
        this.loading.set(false);
      },

      error: () => {
        this.errorMessage.set(
          'Impossible de charger les secteurs.'
        );
        this.loading.set(false);
      }
    });
  }

  loadMunicipalities(): void {
    this.municipalityService.getAll().subscribe({
      next: (data) => {
        this.municipalities.set(data);
      },

      error: () => {
        this.formErrorMessage.set(
          'Impossible de charger les municipalités.'
        );
      }
    });
  }

  openForm(): void {
    this.successMessage.set('');
    this.formErrorMessage.set('');

    this.sectorForm.reset({
      code: '',
      name: '',
      description: '',
      municipalityId: null
    });

    this.showForm.set(true);
  }

  closeForm(): void {
    this.showForm.set(false);
    this.formErrorMessage.set('');
  }

  submitSector(): void {
    this.formErrorMessage.set('');
    this.successMessage.set('');

    if (this.sectorForm.invalid) {
      this.sectorForm.markAllAsTouched();

      this.formErrorMessage.set(
        'Veuillez corriger les champs du formulaire.'
      );

      return;
    }

    const formValue = this.sectorForm.getRawValue();

    if (
      !formValue.code?.trim() ||
      !formValue.name?.trim() ||
      formValue.municipalityId === null
    ) {
      this.formErrorMessage.set(
        'Le code, le nom et la municipalité sont obligatoires.'
      );

      return;
    }

    const request: CreateSectorRequest = {
      code: formValue.code.trim(),
      name: formValue.name.trim(),
      description: formValue.description?.trim() || null,
      municipalityId: Number(formValue.municipalityId)
    };

    this.saving.set(true);

    this.sectorService.create(request).subscribe({
      next: () => {
        this.saving.set(false);
        this.showForm.set(false);

        this.successMessage.set(
          'Le secteur a été créé avec succès.'
        );

        this.loadSectors();
      },

      error: (error) => {
        this.saving.set(false);

        if (error.status === 400) {
          this.formErrorMessage.set(
            'Les données du secteur sont invalides.'
          );
          return;
        }

        if (error.status === 404) {
          this.formErrorMessage.set(
            'La municipalité sélectionnée est introuvable.'
          );
          return;
        }

        if (error.status === 409) {
          this.formErrorMessage.set(
            'Un secteur avec ce code existe déjà dans cette municipalité.'
          );
          return;
        }

        this.formErrorMessage.set(
          'Une erreur est survenue lors de la création du secteur.'
        );
      }
    });
  }

  get municipalityCount(): number {
    return new Set(
      this.sectors().map(sector => sector.municipalityId)
    ).size;
  }

  get sectorsWithDescriptionCount(): number {
    return this.sectors()
      .filter(
        sector =>
          sector.description !== null &&
          sector.description.trim().length > 0
      )
      .length;
  }
}
