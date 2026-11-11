import { ref, watch, onMounted, onUnmounted, toValue, type MaybeRefOrGetter, type Ref } from 'vue';
import { reduxStore, unwrapApiResult } from './reduxStore';

export interface UseRtkQueryResult<TData> {
  data: Ref<TData | undefined>;
  isLoading: Ref<boolean>;
  isFetching: Ref<boolean>;
  isSuccess: Ref<boolean>;
  isError: Ref<boolean>;
  error: Ref<unknown>;
  refetch: () => void;
}

export interface UseRtkQueryOptions {
  skip?: MaybeRefOrGetter<boolean>;
  pollingInterval?: number;
}

export function useRtkQuery<TData, TArg = void>(
  endpoint: {
    initiate: (
      arg: TArg,
      options?: Record<string, unknown>,
    ) => {
      unwrap: () => Promise<TData>;
      refetch: () => void;
      unsubscribe: () => void;
    };
    select: (arg: TArg) => (state: unknown) => {
      data?: TData;
      isLoading?: boolean;
      isFetching?: boolean;
      isSuccess?: boolean;
      isError?: boolean;
      error?: unknown;
    };
  },
  arg?: MaybeRefOrGetter<TArg>,
  options: UseRtkQueryOptions = {},
): UseRtkQueryResult<TData> {
  const data = ref<TData | undefined>(undefined) as Ref<TData | undefined>;
  const isLoading = ref(true);
  const isFetching = ref(false);
  const isSuccess = ref(false);
  const isError = ref(false);
  const error = ref<unknown>(null);

  let currentSubscription: ReturnType<typeof endpoint.initiate> | null = null;
  let unsubscribeStore: (() => void) | null = null;

  function updateFromState() {
    const state = reduxStore.getState();
    const currentArg = toValue(arg) as TArg;
    const selectFn = endpoint.select(currentArg);
    const result = selectFn(state);

    if (result) {
      if (result.data !== undefined) {
        data.value = result.data;
      }
      if (result.isLoading !== undefined) {
        isLoading.value = result.isLoading;
      }
      if (result.isFetching !== undefined) {
        isFetching.value = result.isFetching;
      }
      if (result.isSuccess !== undefined) {
        isSuccess.value = result.isSuccess;
      }
      if (result.isError !== undefined) {
        isError.value = result.isError;
      }
      if (result.error !== undefined) {
        error.value = result.error;
      }
    }
  }

  function subscribe() {
    cleanup();

    const skip = toValue(options.skip);
    if (skip) {
      isLoading.value = false;
      return;
    }

    const currentArg = toValue(arg) as TArg;
    currentSubscription = endpoint.initiate(currentArg, {
      subscribe: true,
      pollingInterval: options.pollingInterval,
    });
    reduxStore.dispatch(currentSubscription as never);

    unsubscribeStore = reduxStore.subscribe(updateFromState);
    updateFromState();
  }

  function cleanup() {
    if (unsubscribeStore) {
      unsubscribeStore();
      unsubscribeStore = null;
    }
    if (currentSubscription) {
      currentSubscription.unsubscribe();
      currentSubscription = null;
    }
  }

  function refetch() {
    if (currentSubscription && typeof currentSubscription.refetch === 'function') {
      currentSubscription.refetch();
    } else {
      subscribe();
    }
  }

  onMounted(subscribe);
  onUnmounted(cleanup);

  watch(
    () => [toValue(arg), toValue(options.skip)],
    () => {
      subscribe();
    },
    { deep: true },
  );

  return {
    data,
    isLoading,
    isFetching,
    isSuccess,
    isError,
    error,
    refetch,
  };
}

export interface UseRtkMutationResult<TData, TArg> {
  trigger: (arg: TArg) => Promise<TData>;
  data: Ref<TData | undefined>;
  isLoading: Ref<boolean>;
  isSuccess: Ref<boolean>;
  isError: Ref<boolean>;
  error: Ref<unknown>;
  reset: () => void;
}

export function useRtkMutation<TData, TArg = void>(endpoint: {
  initiate: (arg: TArg) => {
    unwrap: () => Promise<TData>;
  };
}): UseRtkMutationResult<TData, TArg> {
  const data = ref<TData | undefined>(undefined) as Ref<TData | undefined>;
  const isLoading = ref(false);
  const isSuccess = ref(false);
  const isError = ref(false);
  const error = ref<unknown>(null);

  async function trigger(arg: TArg): Promise<TData> {
    isLoading.value = true;
    isSuccess.value = false;
    isError.value = false;
    error.value = null;

    try {
      const action = endpoint.initiate(arg);
      reduxStore.dispatch(action as never);
      const result = await unwrapApiResult(action);
      data.value = result;
      isSuccess.value = true;
      return result;
    } catch (err) {
      isError.value = true;
      error.value = err;
      throw err;
    } finally {
      isLoading.value = false;
    }
  }

  function reset() {
    isLoading.value = false;
    isSuccess.value = false;
    isError.value = false;
    error.value = null;
    data.value = undefined;
  }

  return {
    trigger,
    data,
    isLoading,
    isSuccess,
    isError,
    error,
    reset,
  };
}
