import { defineStore } from 'pinia';
import { ref } from 'vue';
import { ringsApi } from '@/api/ringsApi';
import { queryClient } from '@/api/queryClient';
import type { RingResponse, RingCreateRequest, RingUpdateRequest } from '@/types/ring';
import { useNotificationStore } from './notificationStore';

export const useRingsStore = defineStore('rings', () => {
  const rings = ref<RingResponse[]>([]);
  const loading = ref(false);
  const error = ref<string | null>(null);
  const notificationStore = useNotificationStore();

  async function fetchRings(force = false) {
    loading.value = true;
    error.value = null;
    try {
      if (force) {
        rings.value = await queryClient.fetchQuery({
          queryKey: ['rings'],
          queryFn: () => ringsApi.getAll(),
          staleTime: 0,
        });
      } else {
        rings.value = await queryClient.ensureQueryData({
          queryKey: ['rings'],
          queryFn: () => ringsApi.getAll(),
        });
      }
    } catch (err) {
      error.value = err instanceof Error ? err.message : 'Не удалось загрузить список колец';
      throw err;
    } finally {
      loading.value = false;
    }
  }

  async function createRing(data: RingCreateRequest): Promise<RingResponse> {
    const created = await ringsApi.create(data);
    notificationStore.addToast({
      type: 'success',
      title: 'Кольцо создано',
      message: `Кольцо "${created.name}" (Сила: ${created.power}) успешно добавлено`,
    });
    return created;
  }

  async function updateRing(id: number, data: RingUpdateRequest): Promise<RingResponse> {
    const updated = await ringsApi.update(id, data);
    notificationStore.addToast({
      type: 'success',
      title: 'Кольцо обновлено',
      message: `Кольцо "${updated.name}" успешно сохранено`,
    });
    return updated;
  }

  async function deleteRing(id: number): Promise<void> {
    const ring = rings.value.find((r) => r.id === id);
    const ringName = ring?.name || `#${id}`;
    await ringsApi.delete(id);
    notificationStore.addToast({
      type: 'info',
      title: 'Кольцо удалено',
      message: `Кольцо "${ringName}" удалено`,
    });
  }

  return {
    rings,
    loading,
    error,
    fetchRings,
    createRing,
    updateRing,
    deleteRing,
  };
});
