export type AnomalyType =
  | 'OVERFLOW'
  | 'DAMAGED'
  | 'ACCESS_BLOCKED'
  | 'ILLEGAL_DUMPING'
  | 'SENSOR_FAILURE'
  | 'OTHER';

export type AnomalySeverity =
  | 'LOW'
  | 'MEDIUM'
  | 'HIGH'
  | 'CRITICAL';

export type AnomalyStatus =
  | 'REPORTED'
  | 'IN_PROGRESS'
  | 'RESOLVED'
  | 'CLOSED';

export interface Anomaly {
  id: number;
  containerId: number;
  containerCode: string;
  sectorId: number;
  sectorName: string;
  municipalityId: number;
  municipalityName: string;
  type: AnomalyType;
  description: string;
  severity: AnomalySeverity;
  status: AnomalyStatus;
  reportedAt: string;
  resolvedAt: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface CreateAnomalyRequest {
  containerId: number;
  type: AnomalyType;
  description: string;
  severity: AnomalySeverity;
  status: AnomalyStatus | null;
  reportedAt: string | null;
}
