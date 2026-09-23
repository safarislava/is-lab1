export type BookCreatureType = 'HOBBIT' | 'ELF' | 'HUMAN' | 'GOLLUM' | 'ORC';

export interface MagicCityResponse {
  id: number;
  name: string;
  area: number;
  population: number;
  establishmentDate?: string | null;
  governor?: BookCreatureType | null;
  capital?: boolean | null;
  populationDensity?: number | null;
}

export interface MagicCityCreateRequest {
  name: string;
  area: number;
  population: number;
  establishmentDate?: string | null;
  governor?: BookCreatureType | null;
  capital?: boolean | null;
  populationDensity?: number | null;
}

export interface MagicCityUpdateRequest {
  name: string;
  area: number;
  population: number;
  establishmentDate?: string | null;
  governor?: BookCreatureType | null;
  capital?: boolean | null;
  populationDensity?: number | null;
}
