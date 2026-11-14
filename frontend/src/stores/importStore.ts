import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { objectsApi } from '@/api/objectsApi';
import { queryClient } from '@/api/queryClient';
import { useCreaturesStore } from './creaturesStore';
import { useCitiesStore } from './citiesStore';
import { useRingsStore } from './ringsStore';
import { useNotificationStore } from './notificationStore';
import { ApiError } from '@/api/httpClient';
import type {
  BatchObjectsCreateRequest,
  BatchObjectsResponse,
  ImportOperationResponse,
} from '@/types/batch';
import { parseAndValidateImport, SAMPLE_JSON } from '@/utils/importParser';

export const useImportStore = defineStore('import', () => {
  const rawContent = ref<string>('');
  const fileName = ref<string | null>(null);
  const fileSize = ref<number | null>(null);

  const validationErrors = ref<string[]>([]);
  const parsedRequest = ref<BatchObjectsCreateRequest | null>(null);
  const isParsedValid = ref<boolean>(false);

  const loading = ref<boolean>(false);
  const serverError = ref<string | null>(null);
  const serverErrorDetails = ref<string[]>([]);
  const lastResult = ref<BatchObjectsResponse | null>(null);

  const history = ref<ImportOperationResponse[]>([]);
  const historyTotal = ref<number>(0);
  const historyPage = ref<number>(0);
  const historySize = ref<number>(10);
  const historyLoading = ref<boolean>(false);

  const creaturesStore = useCreaturesStore();
  const citiesStore = useCitiesStore();
  const ringsStore = useRingsStore();
  const notificationStore = useNotificationStore();

  const previewCounts = computed(() => ({
    creatures: parsedRequest.value?.creatures.length ?? 0,
    cities: parsedRequest.value?.cities.length ?? 0,
    rings: parsedRequest.value?.rings.length ?? 0,
  }));

  const historyTotalPages = computed(() =>
    Math.ceil(historyTotal.value / (historySize.value || 1)),
  );

  function setContent(content: string, name?: string, size?: number) {
    rawContent.value = content;
    fileName.value = name ?? null;
    fileSize.value = size ?? null;
    serverError.value = null;
    serverErrorDetails.value = [];
    lastResult.value = null;
    validate();
  }

  function loadSample() {
    fileName.value = 'sample_import.json';
    rawContent.value = SAMPLE_JSON;
    fileSize.value = new Blob([SAMPLE_JSON]).size;
    serverError.value = null;
    serverErrorDetails.value = [];
    lastResult.value = null;
    validate();
  }

  function validate(): boolean {
    serverError.value = null;
    serverErrorDetails.value = [];

    const res = parseAndValidateImport(rawContent.value);
    validationErrors.value = res.errors;
    parsedRequest.value = res.request;
    isParsedValid.value = res.errors.length === 0 && res.request !== null;
    return isParsedValid.value;
  }

  async function fetchHistory(page: number = historyPage.value, size: number = historySize.value) {
    historyLoading.value = true;
    try {
      const response = await objectsApi.getHistory(page, size);
      history.value = response.content;
      historyTotal.value = response.total;
      historyPage.value = response.page;
      historySize.value = response.size;
    } catch (err) {
      console.error('Failed to fetch import history:', err);
    } finally {
      historyLoading.value = false;
    }
  }

  async function executeImport(): Promise<BatchObjectsResponse> {
    serverError.value = null;
    serverErrorDetails.value = [];
    lastResult.value = null;

    if (!validate() || !parsedRequest.value) {
      throw new Error(validationErrors.value[0] || 'Некорректный JSON');
    }

    loading.value = true;
    try {
      const response = await objectsApi.createBatch(parsedRequest.value);
      lastResult.value = response;

      queryClient.invalidateQueries({ queryKey: ['creatures'] });
      queryClient.invalidateQueries({ queryKey: ['cities'] });
      queryClient.invalidateQueries({ queryKey: ['rings'] });
      queryClient.invalidateQueries({ queryKey: ['import-history'] });

      creaturesStore.fetchCreatures(true).catch(() => {});
      citiesStore.fetchCities(true).catch(() => {});
      ringsStore.fetchRings(true).catch(() => {});
      fetchHistory().catch(() => {});

      const creaturesCount = response.creatures?.length ?? 0;
      const citiesCount = response.cities?.length ?? 0;
      const ringsCount = response.rings?.length ?? 0;

      notificationStore.addToast({
        type: 'success',
        title: 'Импорт завершен',
        message: `Создано: существ — ${creaturesCount}, городов — ${citiesCount}, колец — ${ringsCount}`,
        duration: 5000,
      });

      return response;
    } catch (err) {
      fetchHistory().catch(() => {});

      if (err instanceof ApiError) {
        serverError.value = err.message;
        serverErrorDetails.value = err.details || [];
      } else {
        serverError.value = err instanceof Error ? err.message : 'Ошибка импорта';
        serverErrorDetails.value = [];
      }

      notificationStore.addToast({
        type: 'error',
        title: 'Ошибка импорта',
        message: 'Объекты не сохранены',
        duration: 5000,
      });

      throw err;
    } finally {
      loading.value = false;
    }
  }

  function reset() {
    rawContent.value = '';
    fileName.value = null;
    fileSize.value = null;
    validationErrors.value = [];
    parsedRequest.value = null;
    isParsedValid.value = false;
    serverError.value = null;
    serverErrorDetails.value = [];
    lastResult.value = null;
    loading.value = false;
  }

  validate();

  return {
    rawContent,
    fileName,
    fileSize,
    validationErrors,
    parsedRequest,
    isParsedValid,
    previewCounts,
    loading,
    serverError,
    serverErrorDetails,
    lastResult,
    history,
    historyTotal,
    historyPage,
    historySize,
    historyLoading,
    historyTotalPages,
    setContent,
    loadSample,
    validate,
    fetchHistory,
    executeImport,
    reset,
  };
});
