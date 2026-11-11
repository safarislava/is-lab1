import { apiSlice } from './apiSlice';
import { queryEndpoint, mutateEndpoint } from './reduxStore';
import type { PageResponse } from '@/types/common';
import type {
  BookCreatureResponse,
  BookCreatureCreateRequest,
  BookCreatureUpdateRequest,
  CreatureSearchParams,
  CountResponse,
  AverageResponse,
} from '@/types/creature';

export const creaturesApi = {
  search: (params: CreatureSearchParams = {}): Promise<PageResponse<BookCreatureResponse>> =>
    queryEndpoint(apiSlice.endpoints.getCreatures, params),

  getById: (id: number): Promise<BookCreatureResponse> =>
    queryEndpoint(apiSlice.endpoints.getCreatureById, id),

  create: (data: BookCreatureCreateRequest): Promise<BookCreatureResponse> =>
    mutateEndpoint(apiSlice.endpoints.createCreature, data),

  update: (id: number, data: BookCreatureUpdateRequest): Promise<BookCreatureResponse> =>
    mutateEndpoint(apiSlice.endpoints.updateCreature, { id, data }),

  delete: (id: number): Promise<void> => mutateEndpoint(apiSlice.endpoints.deleteCreature, id),

  deleteByDefenseLevel: (defenseLevel: number): Promise<CountResponse> =>
    mutateEndpoint(apiSlice.endpoints.deleteByDefenseLevel, defenseLevel),

  calculateAverageDefenseLevel: (): Promise<AverageResponse> =>
    queryEndpoint(apiSlice.endpoints.calculateAverageDefenseLevel, undefined),

  findCreaturesWithAttackLevelLessThan: (
    maxAttackLevel: number,
    page: number = 0,
    size: number = 10,
  ): Promise<PageResponse<BookCreatureResponse>> =>
    queryEndpoint(apiSlice.endpoints.findCreaturesWithAttackLevelLessThan, {
      maxAttackLevel,
      page,
      size,
    }),

  takeAllRingsFromHobbits: (): Promise<CountResponse> =>
    mutateEndpoint(apiSlice.endpoints.takeAllRingsFromHobbits, undefined),

  moveHobbitsWithRingsToMordor: (): Promise<CountResponse> =>
    mutateEndpoint(apiSlice.endpoints.moveHobbitsWithRingsToMordor, undefined),
};
