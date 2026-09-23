<script setup lang="ts">
import { useNotificationStore } from '@/stores/notificationStore';
import { Info, CheckCircle2, AlertTriangle, AlertCircle, X } from 'lucide-vue-next';

const notificationStore = useNotificationStore();
</script>

<template>
  <div class="toast-container" aria-live="polite">
    <TransitionGroup name="toast">
      <div
        v-for="toast in notificationStore.toasts"
        :key="toast.id"
        :class="['toast-item', `toast-${toast.type}`]"
      >
        <div class="toast-icon">
          <CheckCircle2 v-if="toast.type === 'success'" :size="18" />
          <AlertTriangle v-else-if="toast.type === 'warning'" :size="18" />
          <AlertCircle v-else-if="toast.type === 'error'" :size="18" />
          <Info v-else :size="18" />
        </div>

        <div class="toast-content">
          <h4 v-if="toast.title" class="toast-title">
            {{ toast.title }}
          </h4>
          <p class="toast-message">
            {{ toast.message }}
          </p>
        </div>

        <button
          class="toast-close"
          aria-label="Close notification"
          @click="notificationStore.removeToast(toast.id)"
        >
          <X :size="14" />
        </button>
      </div>
    </TransitionGroup>
  </div>
</template>

<style scoped>
.toast-container {
  position: fixed;
  bottom: 1.5rem;
  right: 1.5rem;
  z-index: 2000;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  max-width: 380px;
  pointer-events: none;
}

.toast-item {
  pointer-events: auto;
  display: flex;
  align-items: flex-start;
  gap: 0.75rem;
  padding: 0.85rem 1rem;
  border-radius: var(--radius-md);
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  box-shadow: var(--shadow-lg);
  color: var(--text-main);
  transition: all 0.2s ease;
}

.toast-success {
  border-left: 4px solid var(--success);
}
.toast-success .toast-icon {
  color: var(--success);
}

.toast-warning {
  border-left: 4px solid var(--warning);
}
.toast-warning .toast-icon {
  color: var(--warning);
}

.toast-error {
  border-left: 4px solid var(--danger);
}
.toast-error .toast-icon {
  color: var(--danger);
}

.toast-info {
  border-left: 4px solid var(--info);
}
.toast-info .toast-icon {
  color: var(--info);
}

.toast-icon {
  display: flex;
  align-items: center;
  margin-top: 0.1rem;
}

.toast-content {
  flex: 1;
}

.toast-title {
  font-size: 0.875rem;
  font-weight: 600;
  margin: 0 0 0.15rem 0;
}

.toast-message {
  font-size: 0.825rem;
  color: var(--text-muted);
  margin: 0;
  line-height: 1.35;
}

.toast-close {
  background: transparent;
  border: none;
  color: var(--text-dim);
  cursor: pointer;
  padding: 0.2rem;
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: color 0.15s ease;
}

.toast-close:hover {
  color: var(--text-main);
}

.toast-enter-active,
.toast-leave-active {
  transition: all 0.25s ease;
}

.toast-enter-from {
  opacity: 0;
  transform: translateX(30px) scale(0.95);
}

.toast-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>
