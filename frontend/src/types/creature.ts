import type { BookCreatureType, MagicCityResponse } from './city';
import type { CoordinatesRequest, CoordinatesResponse } from './coordinates';
import type { RingResponse } from './ring';
import type { SortDirection } from './common';

export type { BookCreatureType };

export interface BookCreatureResponse {
  id: number;
  name: string;
  coordinates: CoordinatesResponse;
  creationDate: string;
  age: number;
  creatureType: BookCreatureType;
  creatureLocation?: MagicCityResponse | null;
  attackLevel: number;
  defenseLevel: number;
  ring?: RingResponse | null;
}

export interface BookCreatureCreateRequest {
  name: string;
  coordinates: CoordinatesRequest;
  age: number;
  creatureType: BookCreatureType;
  creatureLocationId?: number | null;
  attackLevel: number;
  defenseLevel: number;
  ringId?: number | null;
}

export interface BookCreatureUpdateRequest {
  name: string;
  coordinates: CoordinatesRequest;
  age: number;
  creatureType: BookCreatureType;
  creatureLocationId?: number | null;
  attackLevel: number;
  defenseLevel: number;
  ringId?: number | null;
}

export interface CreatureSearchParams {
  page?: number;
  size?: number;
  sortBy?: string;
  sortDirection?: SortDirection;
  nameFilter?: string;
  creatureTypeFilter?: BookCreatureType | '';
  cityNameFilter?: string;
  ringNameFilter?: string;
}

export interface CountResponse {
  count: number;
}

export interface AverageResponse {
  average: number;
}
