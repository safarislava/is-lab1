import { request, buildQueryString } from './httpClient';
import type { PageResponse } from '@/types/common';
import type {
  BookCreatureResponse,
  BookCreatureCreateRequest,
  BookCreatureUpdateRequest,
  CreatureSearchParams,
  CountResponse,
  AverageResponse,
} from '@/types/creature';

export const creaturesApi = {
  search: (params: CreatureSearchParams = {}): Promise<PageResponse<BookCreatureResponse>> =>
    request<PageResponse<BookCreatureResponse>>(
      `/api/creatures${buildQueryString(params as Record<string, unknown>)}`,
    ),

  getById: (id: number): Promise<BookCreatureResponse> =>
    request<BookCreatureResponse>(`/api/creatures/${id}`),

  create: (data: BookCreatureCreateRequest): Promise<BookCreatureResponse> =>
    request<BookCreatureResponse>('/api/creatures', {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  update: (id: number, data: BookCreatureUpdateRequest): Promise<BookCreatureResponse> =>
    request<BookCreatureResponse>(`/api/creatures/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    }),

  delete: (id: number): Promise<void> =>
    request<void>(`/api/creatures/${id}`, {
      method: 'DELETE',
    }),

  deleteByDefenseLevel: (defenseLevel: number): Promise<CountResponse> =>
    request<CountResponse>(`/api/creatures/special/by-defense?defense_level=${defenseLevel}`, {
      method: 'DELETE',
    }),

  calculateAverageDefenseLevel: (): Promise<AverageResponse> =>
    request<AverageResponse>('/api/creatures/special/avg-defense'),

  findCreaturesWithAttackLevelLessThan: (
    maxAttackLevel: number,
    page: number = 0,
    size: number = 10,
  ): Promise<PageResponse<BookCreatureResponse>> =>
    request<PageResponse<BookCreatureResponse>>(
      `/api/creatures/special/attack-less-than?max_attack_level=${maxAttackLevel}&page=${page}&size=${size}`,
    ),

  takeAllRingsFromHobbits: (): Promise<CountResponse> =>
    request<CountResponse>('/api/creatures/special/take-rings-from-hobbits', {
      method: 'POST',
    }),

  moveHobbitsWithRingsToMordor: (): Promise<CountResponse> =>
    request<CountResponse>('/api/creatures/special/move-hobbits-to-mordor', {
      method: 'POST',
    }),
};
