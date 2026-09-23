import { defineStore } from 'pinia';
import { ref } from 'vue';
import { citiesApi } from '@/api/citiesApi';
import type {
  MagicCityResponse,
  MagicCityCreateRequest,
  MagicCityUpdateRequest,
} from '@/types/city';
import type { NotificationEvent } from '@/types/common';
import { useNotificationStore } from './notificationStore';

export const useCitiesStore = defineStore('cities', () => {
  const cities = ref<MagicCityResponse[]>([]);
  const loading = ref(false);
  const error = ref<string | null>(null);
  const notificationStore = useNotificationStore();

  async function fetchCities() {
    loading.value = true;
    error.value = null;
    try {
      cities.value = await citiesApi.getAll();
    } catch (err) {
      error.value = err instanceof Error ? err.message : 'Failed to fetch cities';
      throw err;
    } finally {
      loading.value = false;
    }
  }

  async function createCity(data: MagicCityCreateRequest): Promise<MagicCityResponse> {
    const created = await citiesApi.create(data);
    const idx = cities.value.findIndex((c) => c.id === created.id);
    if (idx === -1) {
      cities.value.push(created);
    } else {
      cities.value[idx] = created;
    }
    notificationStore.addToast({
      type: 'success',
      title: 'Город создан',
      message: `Город "${created.name}" успешно добавлен`,
    });
    return created;
  }

  async function updateCity(id: number, data: MagicCityUpdateRequest): Promise<MagicCityResponse> {
    const updated = await citiesApi.update(id, data);
    const idx = cities.value.findIndex((c) => c.id === id);
    if (idx !== -1) {
      cities.value[idx] = updated;
    }
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
    cities.value = cities.value.filter((c) => c.id !== id);
    notificationStore.addToast({
      type: 'info',
      title: 'Город удален',
      message: `Город "${cityName}" удален, существа перепривязаны`,
    });
  }

  function handleSseEvent(event: NotificationEvent) {
    if (event.entityType === 'MAGIC_CITY') {
      fetchCities().catch(() => {});
      notificationStore.addToast({
        type: 'info',
        title: 'Синхронизация данных',
        message: `Город #${event.entityId} был изменен на сервере (${event.action})`,
        duration: 3000,
      });
    }
  }

  return {
    cities,
    loading,
    error,
    fetchCities,
    createCity,
    updateCity,
    deleteCity,
    handleSseEvent,
  };
});
