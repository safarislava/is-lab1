import { request } from './httpClient';
import type { BatchObjectsCreateRequest, BatchObjectsResponse } from '@/types/batch';

export const objectsApi = {
  createBatch: (data: BatchObjectsCreateRequest): Promise<BatchObjectsResponse> =>
    request<BatchObjectsResponse>('/api/objects', {
      method: 'POST',
      body: JSON.stringify(data),
    }),
};
