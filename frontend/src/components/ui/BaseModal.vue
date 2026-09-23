<script setup lang="ts">
import { onMounted, onUnmounted, watch } from 'vue';
import { X } from 'lucide-vue-next';

const props = withDefaults(
  defineProps<{
    isOpen: boolean;
    title?: string;
    maxWidth?: string;
  }>(),
  {
    title: '',
    maxWidth: '560px',
  },
);

const emit = defineEmits<{
  (e: 'close'): void;
}>();

function handleKeydown(e: KeyboardEvent) {
  if (e.key === 'Escape' && props.isOpen) {
    emit('close');
  }
}

watch(
  () => props.isOpen,
  (open) => {
    if (open) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = '';
    }
  },
);

onMounted(() => {
  window.addEventListener('keydown', handleKeydown);
});

onUnmounted(() => {
  window.removeEventListener('keydown', handleKeydown);
  document.body.style.overflow = '';
});
</script>

<template>
  <Teleport to="body">
    <Transition name="modal-fade">
      <div v-if="isOpen" class="modal-overlay" @click.self="emit('close')">
        <div
          class="modal-container"
          :style="{ maxWidth: maxWidth }"
          role="dialog"
          aria-modal="true"
        >
          <div class="modal-header">
            <slot name="header">
              <h3 class="modal-title">
                {{ title }}
              </h3>
            </slot>
            <button class="modal-close-btn" aria-label="Close modal" @click="emit('close')">
              <X :size="18" />
            </button>
          </div>

          <div class="modal-body">
            <slot />
          </div>

          <div v-if="$slots.footer" class="modal-footer">
            <slot name="footer" />
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<style scoped>
.modal-overlay {
  position: fixed;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.7);
  backdrop-filter: blur(4px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 1rem;
}

.modal-container {
  background: var(--bg-surface);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-xl);
  width: 100%;
  max-height: 90vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  animation: modalScale 0.2s cubic-bezier(0.16, 1, 0.3, 1);
}

.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1.1rem 1.4rem;
  border-bottom: 1px solid var(--border-color);
  background: var(--bg-card);
}

.modal-title {
  font-size: 1.15rem;
  font-weight: 600;
  color: var(--text-main);
  margin: 0;
}

.modal-close-btn {
  background: transparent;
  border: none;
  color: var(--text-muted);
  cursor: pointer;
  padding: 0.35rem;
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.15s ease;
}

.modal-close-btn:hover {
  background: var(--bg-card-hover);
  color: var(--text-main);
}

.modal-body {
  padding: 1.4rem;
  overflow-y: auto;
  color: var(--text-main);
}

.modal-footer {
  padding: 1rem 1.4rem;
  border-top: 1px solid var(--border-color);
  background: var(--bg-card);
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 0.75rem;
}

.modal-fade-enter-active,
.modal-fade-leave-active {
  transition: opacity 0.2s ease;
}

.modal-fade-enter-from,
.modal-fade-leave-to {
  opacity: 0;
}

@keyframes modalScale {
  from {
    transform: scale(0.96);
    opacity: 0;
  }
  to {
    transform: scale(1);
    opacity: 1;
  }
}
</style>
