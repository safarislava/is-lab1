import { createApi, type BaseQueryFn } from '@reduxjs/toolkit/query';
import { request, buildQueryString, ApiError } from './httpClient';
import type { PageResponse } from '@/types/common';
import type {
  BookCreatureResponse,
  BookCreatureCreateRequest,
  BookCreatureUpdateRequest,
  CreatureSearchParams,
  CountResponse,
  AverageResponse,
} from '@/types/creature';
import type {
  MagicCityResponse,
  MagicCityCreateRequest,
  MagicCityUpdateRequest,
} from '@/types/city';
import type { RingResponse, RingCreateRequest, RingUpdateRequest } from '@/types/ring';

export interface BaseQueryArgs {
  url: string;
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH';
  body?: unknown;
  params?: Record<string, unknown>;
  headers?: Record<string, string>;
}

export interface SerializedApiError {
  message: string;
  status: number;
  details: string[];
}

const customBaseQuery: BaseQueryFn<BaseQueryArgs, unknown, SerializedApiError> = async ({
  url,
  method = 'GET',
  body,
  params,
  headers,
}) => {
  try {
    const qs = params ? buildQueryString(params) : '';
    const fullUrl = `${url}${qs}`;
    const result = await request<unknown>(fullUrl, {
      method,
      headers,
      body: body !== undefined ? JSON.stringify(body) : undefined,
    });
    return { data: result };
  } catch (error) {
    if (error instanceof ApiError) {
      return {
        error: {
          message: error.message,
          status: error.status,
          details: error.details,
        },
      };
    }
    return {
      error: {
        message: error instanceof Error ? error.message : 'Неизвестная ошибка выполнения запроса',
        status: 500,
        details: [],
      },
    };
  }
};

export const apiSlice = createApi({
  reducerPath: 'api',
  baseQuery: customBaseQuery,
  tagTypes: ['Creature', 'City', 'Ring'],
  keepUnusedDataFor: 60,
  endpoints: (builder) => ({
    getCreatures: builder.query<PageResponse<BookCreatureResponse>, CreatureSearchParams | void>({
      query: (params = {}) => ({
        url: '/api/creatures',
        method: 'GET',
        params: {
          page: params?.page ?? 0,
          size: params?.size ?? 10,
          sortBy: params?.sortBy,
          sortDirection: params?.sortDirection,
          nameFilter: params?.nameFilter,
          creatureTypeFilter: params?.creatureTypeFilter,
          cityNameFilter: params?.cityNameFilter,
          ringNameFilter: params?.ringNameFilter,
        },
      }),
      providesTags: (result) =>
        result
          ? [
              ...result.content.map(({ id }) => ({ type: 'Creature' as const, id })),
              { type: 'Creature' as const, id: 'LIST' },
            ]
          : [{ type: 'Creature' as const, id: 'LIST' }],
    }),

    getCreatureById: builder.query<BookCreatureResponse, number>({
      query: (id) => ({
        url: `/api/creatures/${id}`,
        method: 'GET',
      }),
      providesTags: (_result, _error, id) => [{ type: 'Creature' as const, id }],
    }),

    createCreature: builder.mutation<BookCreatureResponse, BookCreatureCreateRequest>({
      query: (data) => ({
        url: '/api/creatures',
        method: 'POST',
        body: data,
      }),
      invalidatesTags: [{ type: 'Creature', id: 'LIST' }],
    }),

    updateCreature: builder.mutation<
      BookCreatureResponse,
      { id: number; data: BookCreatureUpdateRequest }
    >({
      query: ({ id, data }) => ({
        url: `/api/creatures/${id}`,
        method: 'PUT',
        body: data,
      }),
      invalidatesTags: (_result, _error, { id }) => [
        { type: 'Creature', id },
        { type: 'Creature', id: 'LIST' },
      ],
    }),

    deleteCreature: builder.mutation<void, number>({
      query: (id) => ({
        url: `/api/creatures/${id}`,
        method: 'DELETE',
      }),
      invalidatesTags: (_result, _error, id) => [
        { type: 'Creature', id },
        { type: 'Creature', id: 'LIST' },
      ],
    }),

    deleteByDefenseLevel: builder.mutation<CountResponse, number>({
      query: (defenseLevel) => ({
        url: '/api/creatures/special/by-defense',
        method: 'DELETE',
        params: { defense_level: defenseLevel },
      }),
      invalidatesTags: [{ type: 'Creature', id: 'LIST' }],
    }),

    calculateAverageDefenseLevel: builder.query<AverageResponse, void>({
      query: () => ({
        url: '/api/creatures/special/avg-defense',
        method: 'GET',
      }),
      providesTags: [{ type: 'Creature', id: 'LIST' }],
    }),

    findCreaturesWithAttackLevelLessThan: builder.query<
      PageResponse<BookCreatureResponse>,
      { maxAttackLevel: number; page?: number; size?: number }
    >({
      query: ({ maxAttackLevel, page = 0, size = 10 }) => ({
        url: '/api/creatures/special/attack-less-than',
        method: 'GET',
        params: {
          max_attack_level: maxAttackLevel,
          page,
          size,
        },
      }),
      providesTags: [{ type: 'Creature', id: 'LIST' }],
    }),

    takeAllRingsFromHobbits: builder.mutation<CountResponse, void>({
      query: () => ({
        url: '/api/creatures/special/take-rings-from-hobbits',
        method: 'POST',
      }),
      invalidatesTags: [
        { type: 'Creature', id: 'LIST' },
        { type: 'Ring', id: 'LIST' },
      ],
    }),

    moveHobbitsWithRingsToMordor: builder.mutation<CountResponse, void>({
      query: () => ({
        url: '/api/creatures/special/move-hobbits-to-mordor',
        method: 'POST',
      }),
      invalidatesTags: [{ type: 'Creature', id: 'LIST' }],
    }),

    getCities: builder.query<MagicCityResponse[], void>({
      query: () => ({
        url: '/api/cities',
        method: 'GET',
      }),
      providesTags: (result) =>
        result
          ? [
              ...result.map(({ id }) => ({ type: 'City' as const, id })),
              { type: 'City' as const, id: 'LIST' },
            ]
          : [{ type: 'City' as const, id: 'LIST' }],
    }),

    getCityById: builder.query<MagicCityResponse, number>({
      query: (id) => ({
        url: `/api/cities/${id}`,
        method: 'GET',
      }),
      providesTags: (_result, _error, id) => [{ type: 'City' as const, id }],
    }),

    createCity: builder.mutation<MagicCityResponse, MagicCityCreateRequest>({
      query: (data) => ({
        url: '/api/cities',
        method: 'POST',
        body: data,
      }),
      invalidatesTags: [{ type: 'City', id: 'LIST' }],
    }),

    updateCity: builder.mutation<MagicCityResponse, { id: number; data: MagicCityUpdateRequest }>({
      query: ({ id, data }) => ({
        url: `/api/cities/${id}`,
        method: 'PUT',
        body: data,
      }),
      invalidatesTags: (_result, _error, { id }) => [
        { type: 'City', id },
        { type: 'City', id: 'LIST' },
        { type: 'Creature', id: 'LIST' },
      ],
    }),

    deleteCity: builder.mutation<void, { id: number; replacementCityId: number }>({
      query: ({ id, replacementCityId }) => ({
        url: `/api/cities/${id}`,
        method: 'DELETE',
        params: { replacementCityId },
      }),
      invalidatesTags: (_result, _error, { id }) => [
        { type: 'City', id },
        { type: 'City', id: 'LIST' },
        { type: 'Creature', id: 'LIST' },
      ],
    }),

    getRings: builder.query<RingResponse[], void>({
      query: () => ({
        url: '/api/rings',
        method: 'GET',
      }),
      providesTags: (result) =>
        result
          ? [
              ...result.map(({ id }) => ({ type: 'Ring' as const, id })),
              { type: 'Ring' as const, id: 'LIST' },
            ]
          : [{ type: 'Ring' as const, id: 'LIST' }],
    }),

    getRingById: builder.query<RingResponse, number>({
      query: (id) => ({
        url: `/api/rings/${id}`,
        method: 'GET',
      }),
      providesTags: (_result, _error, id) => [{ type: 'Ring', id }],
    }),

    createRing: builder.mutation<RingResponse, RingCreateRequest>({
      query: (data) => ({
        url: '/api/rings',
        method: 'POST',
        body: data,
      }),
      invalidatesTags: [{ type: 'Ring', id: 'LIST' }],
    }),

    updateRing: builder.mutation<RingResponse, { id: number; data: RingUpdateRequest }>({
      query: ({ id, data }) => ({
        url: `/api/rings/${id}`,
        method: 'PUT',
        body: data,
      }),
      invalidatesTags: (_result, _error, { id }) => [
        { type: 'Ring', id },
        { type: 'Ring', id: 'LIST' },
        { type: 'Creature', id: 'LIST' },
      ],
    }),

    deleteRing: builder.mutation<void, number>({
      query: (id) => ({
        url: `/api/rings/${id}`,
        method: 'DELETE',
      }),
      invalidatesTags: (_result, _error, id) => [
        { type: 'Ring', id },
        { type: 'Ring', id: 'LIST' },
        { type: 'Creature', id: 'LIST' },
      ],
    }),
  }),
});
