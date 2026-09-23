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

const BASE_URL = '/api/creatures';

export const creaturesApi = {
  async search(params: CreatureSearchParams = {}): Promise<PageResponse<BookCreatureResponse>> {
    const qs = buildQueryString({
      page: params.page ?? 0,
      size: params.size ?? 10,
      sortBy: params.sortBy,
      sortDirection: params.sortDirection,
      nameFilter: params.nameFilter,
      creatureTypeFilter: params.creatureTypeFilter,
      cityNameFilter: params.cityNameFilter,
      ringNameFilter: params.ringNameFilter,
    });
    return request<PageResponse<BookCreatureResponse>>(`${BASE_URL}${qs}`);
  },

  async getById(id: number): Promise<BookCreatureResponse> {
    return request<BookCreatureResponse>(`${BASE_URL}/${id}`);
  },

  async create(data: BookCreatureCreateRequest): Promise<BookCreatureResponse> {
    return request<BookCreatureResponse>(BASE_URL, {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  async update(id: number, data: BookCreatureUpdateRequest): Promise<BookCreatureResponse> {
    return request<BookCreatureResponse>(`${BASE_URL}/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  },

  async delete(id: number): Promise<void> {
    return request<void>(`${BASE_URL}/${id}`, {
      method: 'DELETE',
    });
  },

  // Special Operations
  async deleteByDefenseLevel(defenseLevel: number): Promise<CountResponse> {
    const qs = buildQueryString({ defense_level: defenseLevel });
    return request<CountResponse>(`${BASE_URL}/special/by-defense${qs}`, {
      method: 'DELETE',
    });
  },

  async calculateAverageDefenseLevel(): Promise<AverageResponse> {
    return request<AverageResponse>(`${BASE_URL}/special/avg-defense`);
  },

  async findCreaturesWithAttackLevelLessThan(
    maxAttackLevel: number,
  ): Promise<BookCreatureResponse[]> {
    const qs = buildQueryString({ max_attack_level: maxAttackLevel });
    return request<BookCreatureResponse[]>(`${BASE_URL}/special/attack-less-than${qs}`);
  },

  async takeAllRingsFromHobbits(): Promise<CountResponse> {
    return request<CountResponse>(`${BASE_URL}/special/take-rings-from-hobbits`, {
      method: 'POST',
    });
  },

  async moveHobbitsWithRingsToMordor(): Promise<CountResponse> {
    return request<CountResponse>(`${BASE_URL}/special/move-hobbits-to-mordor`, {
      method: 'POST',
    });
  },
};
