import { request } from './httpClient';
import type {
  BatchObjectsCreateRequest,
  BatchObjectsResponse,
  ImportOperationPageResponse,
} from '@/types/batch';

export const objectsApi = {
  createBatch: (data: BatchObjectsCreateRequest): Promise<BatchObjectsResponse> =>
    request<BatchObjectsResponse>('/api/objects', {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  getHistory: (page: number = 0, size: number = 10): Promise<ImportOperationPageResponse> =>
    request<ImportOperationPageResponse>(`/api/objects/history?page=${page}&size=${size}`, {
      method: 'GET',
    }),
};
