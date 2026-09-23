import { request } from './httpClient';
import type { RingResponse, RingCreateRequest, RingUpdateRequest } from '@/types/ring';

const BASE_URL = '/api/rings';

export const ringsApi = {
  async getAll(): Promise<RingResponse[]> {
    return request<RingResponse[]>(BASE_URL);
  },

  async getById(id: number): Promise<RingResponse> {
    return request<RingResponse>(`${BASE_URL}/${id}`);
  },

  async create(data: RingCreateRequest): Promise<RingResponse> {
    return request<RingResponse>(BASE_URL, {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  async update(id: number, data: RingUpdateRequest): Promise<RingResponse> {
    return request<RingResponse>(`${BASE_URL}/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  },

  async delete(id: number): Promise<void> {
    return request<void>(`${BASE_URL}/${id}`, {
      method: 'DELETE',
    });
  },
};
