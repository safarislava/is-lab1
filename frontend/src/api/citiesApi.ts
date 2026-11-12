import { request } from './httpClient';
import type {
  MagicCityResponse,
  MagicCityCreateRequest,
  MagicCityUpdateRequest,
} from '@/types/city';

export const citiesApi = {
  getAll: (): Promise<MagicCityResponse[]> => request<MagicCityResponse[]>('/api/cities'),

  getById: (id: number): Promise<MagicCityResponse> =>
    request<MagicCityResponse>(`/api/cities/${id}`),

  create: (data: MagicCityCreateRequest): Promise<MagicCityResponse> =>
    request<MagicCityResponse>('/api/cities', {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  update: (id: number, data: MagicCityUpdateRequest): Promise<MagicCityResponse> =>
    request<MagicCityResponse>(`/api/cities/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    }),

  delete: (id: number, replacementCityId: number): Promise<void> =>
    request<void>(`/api/cities/${id}?replacement_city_id=${replacementCityId}`, {
      method: 'DELETE',
    }),
};
