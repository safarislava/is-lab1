import { request } from './httpClient';
import type { RingResponse, RingCreateRequest, RingUpdateRequest } from '@/types/ring';

export const ringsApi = {
  getAll: (): Promise<RingResponse[]> => request<RingResponse[]>('/api/rings'),

  getById: (id: number): Promise<RingResponse> => request<RingResponse>(`/api/rings/${id}`),

  create: (data: RingCreateRequest): Promise<RingResponse> =>
    request<RingResponse>('/api/rings', {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  update: (id: number, data: RingUpdateRequest): Promise<RingResponse> =>
    request<RingResponse>(`/api/rings/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    }),

  delete: (id: number): Promise<void> =>
    request<void>(`/api/rings/${id}`, {
      method: 'DELETE',
    }),
};
