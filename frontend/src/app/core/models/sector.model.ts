export interface Sector {
  id: number;
  code: string;
  name: string;
  description: string | null;
  municipalityId: number;
  municipalityName: string;
  createdAt: string;
  updatedAt: string;
}
