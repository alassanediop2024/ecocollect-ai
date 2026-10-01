export type ContainerType =
  | 'GENERAL_WASTE'
  | 'RECYCLING'
  | 'ORGANIC'
  | 'GLASS'
  | 'OTHER';

export type ContainerStatus =
  | 'ACTIVE'
  | 'INACTIVE'
  | 'MAINTENANCE'
  | 'FULL'
  | 'DAMAGED';

export interface Container {
  id: number;
  code: string;
  address: string;
  latitude: number | null;
  longitude: number | null;
  containerType: ContainerType;
  capacity: number;
  status: ContainerStatus;
  sectorId: number;
  sectorName: string;
  municipalityId: number;
  municipalityName: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateContainerRequest {
  code: string;
  address: string;
  latitude: number | null;
  longitude: number | null;
  containerType: ContainerType;
  capacity: number;
  status: ContainerStatus;
  sectorId: number;
}
