import { apiSlice } from './apiSlice';
import { reduxStore, unwrapApiResult } from './reduxStore';
import type {
  MagicCityResponse,
  MagicCityCreateRequest,
  MagicCityUpdateRequest,
} from '@/types/city';

export const citiesApi = {
  async getAll(): Promise<MagicCityResponse[]> {
    const action = reduxStore.dispatch(apiSlice.endpoints.getCities.initiate());
    return unwrapApiResult(action);
  },

  async getById(id: number): Promise<MagicCityResponse> {
    const action = reduxStore.dispatch(apiSlice.endpoints.getCityById.initiate(id));
    return unwrapApiResult(action);
  },

  async create(data: MagicCityCreateRequest): Promise<MagicCityResponse> {
    const action = reduxStore.dispatch(apiSlice.endpoints.createCity.initiate(data));
    return unwrapApiResult(action);
  },

  async update(id: number, data: MagicCityUpdateRequest): Promise<MagicCityResponse> {
    const action = reduxStore.dispatch(apiSlice.endpoints.updateCity.initiate({ id, data }));
    return unwrapApiResult(action);
  },

  async delete(id: number, replacementCityId: number): Promise<void> {
    const action = reduxStore.dispatch(
      apiSlice.endpoints.deleteCity.initiate({ id, replacementCityId }),
    );
    return unwrapApiResult(action);
  },
};
