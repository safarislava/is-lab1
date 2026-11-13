import type { BookCreatureType, BookCreatureResponse } from './creature';
import type { CoordinatesRequest } from './coordinates';
import type { MagicCityResponse } from './city';
import type { RingResponse } from './ring';

export interface BatchRingCreateRequest {
  tempId?: number | null;
  name: string;
  power: number;
}

export interface BatchMagicCityCreateRequest {
  tempId?: number | null;
  name: string;
  area: number;
  population: number;
  establishmentDate?: string | null;
  governor?: BookCreatureType | null;
  capital?: boolean | null;
  populationDensity?: number | null;
}

export interface BatchBookCreatureCreateRequest {
  name: string;
  coordinates: CoordinatesRequest;
  age: number;
  creatureType: BookCreatureType;
  attackLevel: number;
  defenseLevel: number;
  creatureLocationId?: number | null;
  tempCreatureLocationId?: number | null;
  ringId?: number | null;
  tempRingId?: number | null;
}

export interface BatchObjectsCreateRequest {
  creatures: BatchBookCreatureCreateRequest[];
  rings: BatchRingCreateRequest[];
  cities: BatchMagicCityCreateRequest[];
}

export interface BatchObjectsResponse {
  cities: MagicCityResponse[];
  rings: RingResponse[];
  creatures: BookCreatureResponse[];
}
