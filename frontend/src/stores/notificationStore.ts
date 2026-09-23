import { defineStore } from 'pinia';
import { ref } from 'vue';
import type { SseConnectionStatus } from '@/api/sseClient';

export interface ToastMessage {
  id: string;
  type: 'info' | 'success' | 'warning' | 'error';
  title?: string;
  message: string;
  duration?: number;
}

export const useNotificationStore = defineStore('notification', () => {
  const connectionStatus = ref<SseConnectionStatus>('CONNECTING');
  const toasts = ref<ToastMessage[]>([]);

  function setStatus(status: SseConnectionStatus) {
    connectionStatus.value = status;
  }

  function addToast(toast: Omit<ToastMessage, 'id'>): string {
    const id = `${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
    const duration = toast.duration ?? 4000;
    const newToast: ToastMessage = { ...toast, id, duration };
    toasts.value.push(newToast);

    if (duration > 0) {
      setTimeout(() => {
        removeToast(id);
      }, duration);
    }
    return id;
  }

  function removeToast(id: string) {
    toasts.value = toasts.value.filter((t) => t.id !== id);
  }

  return {
    connectionStatus,
    toasts,
    setStatus,
    addToast,
    removeToast,
  };
});
