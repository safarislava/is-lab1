import { defineStore } from 'pinia';
import { ref } from 'vue';
import { ringsApi } from '@/api/ringsApi';
import type { RingResponse, RingCreateRequest, RingUpdateRequest } from '@/types/ring';
import type { NotificationEvent } from '@/types/common';
import { useNotificationStore } from './notificationStore';

export const useRingsStore = defineStore('rings', () => {
  const rings = ref<RingResponse[]>([]);
  const loading = ref(false);
  const error = ref<string | null>(null);
  const notificationStore = useNotificationStore();

  async function fetchRings() {
    loading.value = true;
    error.value = null;
    try {
      rings.value = await ringsApi.getAll();
    } catch (err) {
      error.value = err instanceof Error ? err.message : 'Failed to fetch rings';
      throw err;
    } finally {
      loading.value = false;
    }
  }

  async function createRing(data: RingCreateRequest): Promise<RingResponse> {
    const created = await ringsApi.create(data);
    const idx = rings.value.findIndex((r) => r.id === created.id);
    if (idx === -1) {
      rings.value.push(created);
    } else {
      rings.value[idx] = created;
    }
    notificationStore.addToast({
      type: 'success',
      title: 'Кольцо создано',
      message: `Кольцо "${created.name}" (Сила: ${created.power}) успешно добавлено`,
    });
    return created;
  }

  async function updateRing(id: number, data: RingUpdateRequest): Promise<RingResponse> {
    const updated = await ringsApi.update(id, data);
    const idx = rings.value.findIndex((r) => r.id === id);
    if (idx !== -1) {
      rings.value[idx] = updated;
    }
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
    rings.value = rings.value.filter((r) => r.id !== id);
    notificationStore.addToast({
      type: 'info',
      title: 'Кольцо удалено',
      message: `Кольцо "${ringName}" удалено`,
    });
  }

  function handleSseEvent(event: NotificationEvent) {
    if (event.entityType === 'RING') {
      fetchRings().catch(() => {});
      notificationStore.addToast({
        type: 'info',
        title: 'Синхронизация данных',
        message: `Кольцо #${event.entityId} было изменено на сервере (${event.action})`,
        duration: 3000,
      });
    }
  }

  return {
    rings,
    loading,
    error,
    fetchRings,
    createRing,
    updateRing,
    deleteRing,
    handleSseEvent,
  };
});
