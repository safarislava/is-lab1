import { request, buildQueryString } from './httpClient';
import type {
  MagicCityResponse,
  MagicCityCreateRequest,
  MagicCityUpdateRequest,
} from '@/types/city';

const BASE_URL = '/api/cities';

export const citiesApi = {
  async getAll(): Promise<MagicCityResponse[]> {
    return request<MagicCityResponse[]>(BASE_URL);
  },

  async getById(id: number): Promise<MagicCityResponse> {
    return request<MagicCityResponse>(`${BASE_URL}/${id}`);
  },

  async create(data: MagicCityCreateRequest): Promise<MagicCityResponse> {
    return request<MagicCityResponse>(BASE_URL, {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  async update(id: number, data: MagicCityUpdateRequest): Promise<MagicCityResponse> {
    return request<MagicCityResponse>(`${BASE_URL}/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  },

  async delete(id: number, replacementCityId: number): Promise<void> {
    const qs = buildQueryString({ replacementCityId });
    return request<void>(`${BASE_URL}/${id}${qs}`, {
      method: 'DELETE',
    });
  },
};
