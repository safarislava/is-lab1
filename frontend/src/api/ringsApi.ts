import { apiSlice } from './apiSlice';
import { queryEndpoint, mutateEndpoint } from './reduxStore';
import type { RingResponse, RingCreateRequest, RingUpdateRequest } from '@/types/ring';

export const ringsApi = {
  getAll: (): Promise<RingResponse[]> =>
    queryEndpoint(apiSlice.endpoints.getRings, undefined),

  getById: (id: number): Promise<RingResponse> =>
    queryEndpoint(apiSlice.endpoints.getRingById, id),

  create: (data: RingCreateRequest): Promise<RingResponse> =>
    mutateEndpoint(apiSlice.endpoints.createRing, data),

  update: (id: number, data: RingUpdateRequest): Promise<RingResponse> =>
    mutateEndpoint(apiSlice.endpoints.updateRing, { id, data }),

  delete: (id: number): Promise<void> =>
    mutateEndpoint(apiSlice.endpoints.deleteRing, id),
};
