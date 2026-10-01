export type CollectionStatus =
  | 'SCHEDULED'
  | 'IN_PROGRESS'
  | 'COMPLETED'
  | 'CANCELLED'
  | 'FAILED';

export interface Collection {
  id: number;
  containerId: number;
  containerCode: string;
  sectorId: number;
  sectorName: string;
  municipalityId: number;
  municipalityName: string;
  collectionDate: string;
  weightKg: number | null;
  status: CollectionStatus | null;
  operator: string | null;
  notes: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface CreateCollectionRequest {
  containerId: number;
  collectionDate: string;
  weightKg: number | null;
  status: CollectionStatus | null;
  operator: string | null;
  notes: string | null;
}
