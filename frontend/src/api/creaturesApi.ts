import { apiSlice } from './apiSlice';
import { reduxStore, unwrapApiResult } from './reduxStore';
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
  async search(params: CreatureSearchParams = {}): Promise<PageResponse<BookCreatureResponse>> {
    const action = reduxStore.dispatch(
      apiSlice.endpoints.getCreatures.initiate(params, { subscribe: false, forceRefetch: true }),
    );
    return unwrapApiResult(action);
  },

  async getById(id: number): Promise<BookCreatureResponse> {
    const action = reduxStore.dispatch(
      apiSlice.endpoints.getCreatureById.initiate(id, { subscribe: false, forceRefetch: true }),
    );
    return unwrapApiResult(action);
  },

  async create(data: BookCreatureCreateRequest): Promise<BookCreatureResponse> {
    const action = reduxStore.dispatch(apiSlice.endpoints.createCreature.initiate(data));
    return unwrapApiResult(action);
  },

  async update(id: number, data: BookCreatureUpdateRequest): Promise<BookCreatureResponse> {
    const action = reduxStore.dispatch(apiSlice.endpoints.updateCreature.initiate({ id, data }));
    return unwrapApiResult(action);
  },

  async delete(id: number): Promise<void> {
    const action = reduxStore.dispatch(apiSlice.endpoints.deleteCreature.initiate(id));
    return unwrapApiResult(action);
  },

  async deleteByDefenseLevel(defenseLevel: number): Promise<CountResponse> {
    const action = reduxStore.dispatch(
      apiSlice.endpoints.deleteByDefenseLevel.initiate(defenseLevel),
    );
    return unwrapApiResult(action);
  },

  async calculateAverageDefenseLevel(): Promise<AverageResponse> {
    const action = reduxStore.dispatch(
      apiSlice.endpoints.calculateAverageDefenseLevel.initiate(undefined, {
        subscribe: false,
        forceRefetch: true,
      }),
    );
    return unwrapApiResult(action);
  },

  async findCreaturesWithAttackLevelLessThan(
    maxAttackLevel: number,
    page: number = 0,
    size: number = 10,
  ): Promise<PageResponse<BookCreatureResponse>> {
    const action = reduxStore.dispatch(
      apiSlice.endpoints.findCreaturesWithAttackLevelLessThan.initiate(
        { maxAttackLevel, page, size },
        { subscribe: false, forceRefetch: true },
      ),
    );
    return unwrapApiResult(action);
  },

  async takeAllRingsFromHobbits(): Promise<CountResponse> {
    const action = reduxStore.dispatch(apiSlice.endpoints.takeAllRingsFromHobbits.initiate());
    return unwrapApiResult(action);
  },

  async moveHobbitsWithRingsToMordor(): Promise<CountResponse> {
    const action = reduxStore.dispatch(apiSlice.endpoints.moveHobbitsWithRingsToMordor.initiate());
    return unwrapApiResult(action);
  },
};
