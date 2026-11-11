import { configureStore, createAction, createListenerMiddleware } from '@reduxjs/toolkit';
import { apiSlice } from './apiSlice';
import { ApiError } from './httpClient';
import type { NotificationEvent } from '@/types/common';

export const sseEventReceived = createAction<NotificationEvent>('sse/eventReceived');

export function invalidateTagsForSseEvent(event: NotificationEvent): void {
  switch (event.entityType) {
    case 'BOOK_CREATURE':
      reduxStore.dispatch(
        apiSlice.util.invalidateTags([
          'Creature',
          { type: 'Creature', id: 'LIST' },
          ...(event.entityId > 0 ? [{ type: 'Creature' as const, id: event.entityId }] : []),
        ]),
      );
      break;

    case 'MAGIC_CITY':
      reduxStore.dispatch(
        apiSlice.util.invalidateTags([
          'City',
          'Creature',
          { type: 'City', id: 'LIST' },
          { type: 'Creature', id: 'LIST' },
          ...(event.entityId > 0 ? [{ type: 'City' as const, id: event.entityId }] : []),
        ]),
      );
      break;

    case 'RING':
      reduxStore.dispatch(
        apiSlice.util.invalidateTags([
          'Ring',
          'Creature',
          { type: 'Ring', id: 'LIST' },
          { type: 'Creature', id: 'LIST' },
          ...(event.entityId > 0 ? [{ type: 'Ring' as const, id: event.entityId }] : []),
        ]),
      );
      break;
  }
}

export const sseListenerMiddleware = createListenerMiddleware();

sseListenerMiddleware.startListening({
  actionCreator: sseEventReceived,
  effect: (action) => {
    invalidateTagsForSseEvent(action.payload);
  },
});

export const reduxStore = configureStore({
  reducer: {
    [apiSlice.reducerPath]: apiSlice.reducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: ['api/executeQuery/rejected', 'api/executeMutation/rejected'],
      },
    }).concat(apiSlice.middleware, sseListenerMiddleware.middleware),
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
    const errObj = rawError as { message?: string; status?: number; details?: string[] } | null;
    const message = errObj?.message || 'Ошибка выполнения запроса';
    const status = typeof errObj?.status === 'number' ? errObj.status : 500;
    const details = Array.isArray(errObj?.details) ? errObj.details : [];
    throw new ApiError(message, status, details);
  }
}
