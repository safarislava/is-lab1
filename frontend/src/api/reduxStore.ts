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
