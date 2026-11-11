import { apiSlice } from './apiSlice';
import { queryEndpoint, mutateEndpoint } from './reduxStore';
import type {
  MagicCityResponse,
  MagicCityCreateRequest,
  MagicCityUpdateRequest,
} from '@/types/city';

export const citiesApi = {
  getAll: (): Promise<MagicCityResponse[]> =>
    queryEndpoint(apiSlice.endpoints.getCities, undefined),

  getById: (id: number): Promise<MagicCityResponse> =>
    queryEndpoint(apiSlice.endpoints.getCityById, id),

  create: (data: MagicCityCreateRequest): Promise<MagicCityResponse> =>
    mutateEndpoint(apiSlice.endpoints.createCity, data),

  update: (id: number, data: MagicCityUpdateRequest): Promise<MagicCityResponse> =>
    mutateEndpoint(apiSlice.endpoints.updateCity, { id, data }),

  delete: (id: number, replacementCityId: number): Promise<void> =>
    mutateEndpoint(apiSlice.endpoints.deleteCity, { id, replacementCityId }),
};
