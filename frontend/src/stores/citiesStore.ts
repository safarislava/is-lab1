import { defineStore } from 'pinia';
import { ref } from 'vue';
import { citiesApi } from '@/api/citiesApi';
import { queryClient } from '@/api/queryClient';
import type {
  MagicCityResponse,
  MagicCityCreateRequest,
  MagicCityUpdateRequest,
} from '@/types/city';
import { useNotificationStore } from './notificationStore';

export const useCitiesStore = defineStore('cities', () => {
  const cities = ref<MagicCityResponse[]>([]);
  const loading = ref(false);
  const error = ref<string | null>(null);
  const notificationStore = useNotificationStore();

  async function fetchCities(force = false) {
    loading.value = true;
    error.value = null;
    try {
      if (force) {
        cities.value = await queryClient.fetchQuery({
          queryKey: ['cities'],
          queryFn: () => citiesApi.getAll(),
          staleTime: 0,
        });
      } else {
        cities.value = await queryClient.ensureQueryData({
          queryKey: ['cities'],
          queryFn: () => citiesApi.getAll(),
        });
      }
    } catch (err) {
      error.value = err instanceof Error ? err.message : 'Не удалось загрузить список городов';
      throw err;
    } finally {
      loading.value = false;
    }
  }

  async function createCity(data: MagicCityCreateRequest): Promise<MagicCityResponse> {
    const created = await citiesApi.create(data);
    notificationStore.addToast({
      type: 'success',
      title: 'Город создан',
      message: `Город "${created.name}" успешно добавлен`,
    });
    return created;
  }

  async function updateCity(id: number, data: MagicCityUpdateRequest): Promise<MagicCityResponse> {
    const updated = await citiesApi.update(id, data);
    notificationStore.addToast({
      type: 'success',
      title: 'Город обновлен',
      message: `Город "${updated.name}" успешно сохранен`,
    });
    return updated;
  }

  async function deleteCity(id: number, replacementCityId: number): Promise<void> {
    const city = cities.value.find((c) => c.id === id);
    const cityName = city?.name || `#${id}`;
    await citiesApi.delete(id, replacementCityId);
    notificationStore.addToast({
      type: 'info',
      title: 'Город удален',
      message: `Город "${cityName}" удален, существа перепривязаны`,
    });
  }

  return {
    cities,
    loading,
    error,
    fetchCities,
    createCity,
    updateCity,
    deleteCity,
  };
});
