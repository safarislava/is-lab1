import { defineStore } from 'pinia';
import { ref } from 'vue';
import { creaturesApi } from '@/api/creaturesApi';
import { queryClient } from '@/api/queryClient';
import type {
  BookCreatureResponse,
  BookCreatureCreateRequest,
  BookCreatureUpdateRequest,
  BookCreatureType,
  CreatureSearchParams,
} from '@/types/creature';
import type { PageResponse, SortDirection } from '@/types/common';
import { useNotificationStore } from './notificationStore';

export const useCreaturesStore = defineStore('creatures', () => {
  const creatures = ref<BookCreatureResponse[]>([]);
  const total = ref(0);
  const page = ref(0);
  const size = ref(10);
  const sortBy = ref<string | null>(null);
  const sortDirection = ref<SortDirection>('ASC');

  const nameFilter = ref('');
  const creatureTypeFilter = ref<BookCreatureType | ''>('');
  const cityNameFilter = ref('');
  const ringNameFilter = ref('');

  const loading = ref(false);
  const error = ref<string | null>(null);

  const notificationStore = useNotificationStore();

  function getQueryParams(): CreatureSearchParams {
    return {
      page: page.value,
      size: size.value,
      sortBy: sortBy.value || undefined,
      sortDirection: sortDirection.value || undefined,
      nameFilter: nameFilter.value.trim() || undefined,
      creatureTypeFilter: creatureTypeFilter.value || undefined,
      cityNameFilter: cityNameFilter.value.trim() || undefined,
      ringNameFilter: ringNameFilter.value.trim() || undefined,
    };
  }

  async function fetchCreatures(force = false) {
    loading.value = true;
    error.value = null;
    const params = getQueryParams();
    const queryKey = ['creatures', params];

    try {
      let result: PageResponse<BookCreatureResponse>;
      if (force) {
        result = await queryClient.fetchQuery({
          queryKey,
          queryFn: () => creaturesApi.search(params),
          staleTime: 0,
        });
      } else {
        result = await queryClient.ensureQueryData({
          queryKey,
          queryFn: () => creaturesApi.search(params),
        });
      }
      creatures.value = result.content;
      total.value = result.total;
    } catch (err) {
      error.value = err instanceof Error ? err.message : 'Не удалось загрузить список существ';
      throw err;
    } finally {
      loading.value = false;
    }
  }

  function setPage(newPage: number) {
    page.value = newPage;
    return fetchCreatures();
  }

  function setSize(newSize: number) {
    size.value = newSize;
    page.value = 0;
    return fetchCreatures();
  }

  function toggleSort(columnKey: string) {
    if (sortBy.value === columnKey) {
      if (sortDirection.value === 'ASC') {
        sortDirection.value = 'DESC';
      } else {
        sortBy.value = null;
        sortDirection.value = 'ASC';
      }
    } else {
      sortBy.value = columnKey;
      sortDirection.value = 'ASC';
    }
    page.value = 0;
    return fetchCreatures();
  }

  function applyFilters() {
    page.value = 0;
    return fetchCreatures();
  }

  function resetFilters() {
    nameFilter.value = '';
    creatureTypeFilter.value = '';
    cityNameFilter.value = '';
    ringNameFilter.value = '';
    page.value = 0;
    return fetchCreatures();
  }

  async function createCreature(data: BookCreatureCreateRequest): Promise<BookCreatureResponse> {
    const created = await creaturesApi.create(data);
    notificationStore.addToast({
      type: 'success',
      title: 'Существо создано',
      message: `Существо "${created.name}" (ID: ${created.id}) успешно добавлено`,
    });
    return created;
  }

  async function updateCreature(
    id: number,
    data: BookCreatureUpdateRequest,
  ): Promise<BookCreatureResponse> {
    const updated = await creaturesApi.update(id, data);
    notificationStore.addToast({
      type: 'success',
      title: 'Существо обновлено',
      message: `Существо "${updated.name}" (ID: ${updated.id}) успешно сохранено`,
    });
    return updated;
  }

  async function deleteCreature(id: number): Promise<void> {
    const creature = creatures.value.find((c) => c.id === id);
    const name = creature?.name || `#${id}`;
    await creaturesApi.delete(id);
    notificationStore.addToast({
      type: 'info',
      title: 'Существо удалено',
      message: `Существо "${name}" удалено`,
    });
  }

  return {
    creatures,
    total,
    page,
    size,
    sortBy,
    sortDirection,
    nameFilter,
    creatureTypeFilter,
    cityNameFilter,
    ringNameFilter,
    loading,
    error,
    fetchCreatures,
    setPage,
    setSize,
    toggleSort,
    applyFilters,
    resetFilters,
    createCreature,
    updateCreature,
    deleteCreature,
  };
});
