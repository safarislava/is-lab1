import { apiSlice } from './apiSlice';
import { reduxStore, unwrapApiResult } from './reduxStore';
import type { RingResponse, RingCreateRequest, RingUpdateRequest } from '@/types/ring';

export const ringsApi = {
  async getAll(): Promise<RingResponse[]> {
    const action = reduxStore.dispatch(
      apiSlice.endpoints.getRings.initiate(undefined, { subscribe: false, forceRefetch: true }),
    );
    return unwrapApiResult(action);
  },

  async getById(id: number): Promise<RingResponse> {
    const action = reduxStore.dispatch(
      apiSlice.endpoints.getRingById.initiate(id, { subscribe: false, forceRefetch: true }),
    );
    return unwrapApiResult(action);
  },

  async create(data: RingCreateRequest): Promise<RingResponse> {
    const action = reduxStore.dispatch(apiSlice.endpoints.createRing.initiate(data));
    return unwrapApiResult(action);
  },

  async update(id: number, data: RingUpdateRequest): Promise<RingResponse> {
    const action = reduxStore.dispatch(apiSlice.endpoints.updateRing.initiate({ id, data }));
    return unwrapApiResult(action);
  },

  async delete(id: number): Promise<void> {
    const action = reduxStore.dispatch(apiSlice.endpoints.deleteRing.initiate(id));
    return unwrapApiResult(action);
  },
};
