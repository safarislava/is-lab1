import { configureStore } from '@reduxjs/toolkit';
import { apiSlice } from './apiSlice';
import { ApiError } from './httpClient';

export const reduxStore = configureStore({
  reducer: {
    [apiSlice.reducerPath]: apiSlice.reducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: ['api/executeQuery/rejected', 'api/executeMutation/rejected'],
      },
    }).concat(apiSlice.middleware),
});

export type RootState = ReturnType<typeof reduxStore.getState>;
export type AppDispatch = typeof reduxStore.dispatch;

export async function unwrapApiResult<T>(promise: { unwrap: () => Promise<T> }): Promise<T> {
  try {
    return await promise.unwrap();
  } catch (rawError: unknown) {
    if (rawError instanceof ApiError) {
      throw rawError;
    }
    const err = rawError as { message?: string; status?: number; details?: string[] } | null;
    throw new ApiError(
      err?.message || 'Ошибка выполнения запроса',
      err?.status ?? 500,
      err?.details ?? [],
    );
  }
}

export async function queryEndpoint<TData = unknown, TArg = unknown>(
  endpoint: {
    initiate: (arg: TArg, options?: { subscribe?: boolean; forceRefetch?: boolean }) => unknown;
  },
  arg: TArg,
): Promise<TData> {
  const dispatch = reduxStore.dispatch as unknown as (action: unknown) => {
    unwrap: () => Promise<TData>;
  };
  const action = dispatch(endpoint.initiate(arg, { subscribe: false, forceRefetch: true }));
  return unwrapApiResult<TData>(action);
}

export async function mutateEndpoint<TData = unknown, TArg = unknown>(
  endpoint: { initiate: (arg: TArg) => unknown },
  arg: TArg,
): Promise<TData> {
  const dispatch = reduxStore.dispatch as unknown as (action: unknown) => {
    unwrap: () => Promise<TData>;
  };
  const action = dispatch(endpoint.initiate(arg));
  return unwrapApiResult<TData>(action);
}
